package com.ateam.calmate.dietManagement.command.service;

import com.ateam.calmate.dietManagement.command.dto.FoodRequest;
import com.ateam.calmate.dietManagement.command.dto.MealRequest;
import com.ateam.calmate.dietManagement.command.entity.ExtendFilePath;
import com.ateam.calmate.dietManagement.command.entity.Food;
import com.ateam.calmate.dietManagement.command.entity.FoodFileUpload;
import com.ateam.calmate.dietManagement.command.entity.Meal;
import com.ateam.calmate.dietManagement.command.repository.ExtendFilePathRepository;
import com.ateam.calmate.dietManagement.command.repository.FoodFileUploadRepository;
import com.ateam.calmate.dietManagement.command.repository.FoodRepository;
import com.ateam.calmate.dietManagement.command.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MealCommandServiceImpl implements MealCommandService {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final FoodFileUploadRepository fileUploadRepository;
    private final ExtendFilePathRepository extendFilePathRepository;

    // 프로젝트 루트 기준 상대 경로
    private final String uploadRootDir = "uploads/meal";

    @Override
    public Long createMeal(MealRequest request, List<MultipartFile> files) {
        Meal meal = Meal.builder()
                .date(request.getDate())
                .type(request.getType())
                .memberId(request.getMemberId())
                .build();

        Food food = buildFoodFromRequest(request.getFood());
        foodRepository.save(food);

        List<Food> foods = new ArrayList<>();
        foods.add(food);
        meal.setFoods(foods);

        mealRepository.save(meal);

        if (files != null && !files.isEmpty()) {
            int order = 1;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                saveFile(meal, file, order++);
            }
        }

        return meal.getId();
    }

    @Override
    public void updateMeal(Long id, MealRequest request, List<MultipartFile> files) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("meal not found"));

        meal.setDate(request.getDate());
        meal.setType(request.getType());
        meal.setMemberId(request.getMemberId());

        // 음식 자체 갱신
        meal.getFoods().clear();
        Food food = buildFoodFromRequest(request.getFood());
        foodRepository.save(food);
        meal.getFoods().add(food);

        List<Long> keepFileIds = request.getKeepFileIds(); // null 이면 “이미지 손대지 않음”

        boolean hasNewFiles = (files != null && !files.isEmpty());
        boolean touchImages = (keepFileIds != null) || hasNewFiles;

        // 이미지 안 건드리면 그대로 둠
        if (!touchImages) {
            return;
        }

        // 기존 파일 중에서 keepFileIds 에 없는 것만 삭제
        List<FoodFileUpload> currentFiles = new ArrayList<>(meal.getFiles());
        for (FoodFileUpload f : currentFiles) {
            boolean keep = (keepFileIds != null && keepFileIds.contains(f.getId()));
            if (!keep) {
                deletePhysicalFile(f);
                meal.getFiles().remove(f);
                fileUploadRepository.delete(f);
            }
        }

        // 새 파일은 남아있는 파일 개수 뒤부터 순서 이어서 저장
        if (hasNewFiles) {
            int order = meal.getFiles().size() + 1;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                saveFile(meal, file, order++);
            }
        }
    }

    @Override
    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("meal not found"));

        List<FoodFileUpload> oldFiles = new ArrayList<>(meal.getFiles());
        for (FoodFileUpload f : oldFiles) {
            deletePhysicalFile(f);
            meal.getFiles().remove(f);
            fileUploadRepository.delete(f);
        }

        mealRepository.delete(meal);
    }

    private Food buildFoodFromRequest(FoodRequest foodReq) {
        if (foodReq == null) {
            throw new IllegalArgumentException("food is required");
        }

        Integer gram = foodReq.getGram() != null ? foodReq.getGram() : 100;
        BigDecimal kcal = foodReq.getKcal() != null ? foodReq.getKcal() : BigDecimal.ZERO;
        BigDecimal carbo = foodReq.getCarbo() != null ? foodReq.getCarbo() : BigDecimal.ZERO;
        BigDecimal protein = foodReq.getProtein() != null ? foodReq.getProtein() : BigDecimal.ZERO;
        BigDecimal fat = foodReq.getFat() != null ? foodReq.getFat() : BigDecimal.ZERO;
        BigDecimal sodium = foodReq.getSodium() != null ? foodReq.getSodium() : BigDecimal.ZERO;

        return Food.builder()
                .name(foodReq.getName())
                .gram(gram)
                .kcal(kcal)
                .carbo(carbo)
                .protein(protein)
                .fat(fat)
                .sodium(sodium)
                .build();
    }

    private void saveFile(Meal meal, MultipartFile file, int order) {
        try {
            String originalName = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(originalName);
            String storeName = UUID.randomUUID() + (ext != null ? "." + ext : "");

            // 실제 저장 경로: {projectRoot}/uploads/meal/{mealId}/
            Path root = Paths.get(uploadRootDir).toAbsolutePath().normalize();
            Path dir = root.resolve(String.valueOf(meal.getId()));
            Files.createDirectories(dir);

            Path filePath = dir.resolve(storeName);
            file.transferTo(filePath.toFile());

            // url_path: /img/meal/{mealId}/
            ExtendFilePath extendFilePath = ExtendFilePath.builder()
                    .urlPath("/img/meal/" + meal.getId() + "/")
                    .build();
            extendFilePathRepository.save(extendFilePath);

            // DB path 에는 mealId 만 저장 (삭제용)
            FoodFileUpload upload = FoodFileUpload.builder()
                    .meal(meal)
                    .name(originalName)
                    .type(file.getContentType())
                    .reName(storeName)
                    .path(String.valueOf(meal.getId()))
                    .thumbPath("")
                    .uploadOrder(order)
                    .extendFilePath(extendFilePath)
                    .build();

            meal.addFile(upload);
            fileUploadRepository.save(upload);
        } catch (Exception e) {
            throw new RuntimeException("file save error: " + e.getMessage(), e);
        }
    }

    private void deletePhysicalFile(FoodFileUpload f) {
        try {
            if (f.getPath() == null || f.getReName() == null) {
                return;
            }
            Path root = Paths.get(uploadRootDir).toAbsolutePath().normalize();
            Path filePath = root
                    .resolve(f.getPath())       // mealId
                    .resolve(f.getReName());    // uuid.xxx
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
