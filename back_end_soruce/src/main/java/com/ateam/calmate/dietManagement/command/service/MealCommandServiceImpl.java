package com.ateam.calmate.dietManagement.command.service;

import com.ateam.calmate.dietManagement.command.dto.FoodRequest;
import com.ateam.calmate.dietManagement.command.dto.MealRequest;
import com.ateam.calmate.dietManagement.command.entity.*;
import com.ateam.calmate.dietManagement.command.repository.*;
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
    private final FoodExtendFilePathRepository extendFilePathRepository;
    private final MealPointService mealPointService; // ✅ 포인트 서비스

    private final String uploadRootDir = "back_end_soruce/img/meal";

    @Override
    public Long createMeal(MealRequest request, List<MultipartFile> files) {
        // ✅ 식사 엔티티 생성
        Meal meal = Meal.builder()
                .date(request.getDate())
                .type(request.getType())
                .memberId(request.getMemberId())
                .build();

        // ✅ 음식 엔티티 생성 및 저장
        Food food = buildFoodFromRequest(request.getFood());
        foodRepository.save(food);

        List<Food> foods = new ArrayList<>();
        foods.add(food);
        meal.setFoods(foods);

        mealRepository.save(meal);

        // ✅ 파일 저장
        if (files != null && !files.isEmpty()) {
            int order = 1;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                saveFile(meal, file, order++);
            }
        }

        // ✅ 작성할 때마다 5포인트 지급
        mealPointService.earnMealPoint(request.getMemberId());

        return meal.getId();
    }

    @Override
    public void updateMeal(Long id, MealRequest request, List<MultipartFile> files) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("meal not found"));

        meal.setDate(request.getDate());
        meal.setType(request.getType());
        meal.setMemberId(request.getMemberId());

        // ✅ 음식 업데이트
        meal.getFoods().clear();
        Food food = buildFoodFromRequest(request.getFood());
        foodRepository.save(food);
        meal.getFoods().add(food);

        List<Long> keepFileIds = request.getKeepFileIds();
        boolean hasNewFiles = (files != null && !files.isEmpty());
        boolean touchImages = (keepFileIds != null) || hasNewFiles;

        if (!touchImages) return;

        // ✅ 기존 파일 중에서 유지 안 하는 것 삭제
        List<FoodFileUpload> currentFiles = new ArrayList<>(meal.getFiles());
        for (FoodFileUpload f : currentFiles) {
            boolean keep = (keepFileIds != null && keepFileIds.contains(f.getId()));
            if (!keep) {
                deletePhysicalFile(f);
                meal.getFiles().remove(f);
                fileUploadRepository.delete(f);
            }
        }

        // ✅ 새 파일 저장
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

            Path root = Paths.get(System.getProperty("user.dir"), uploadRootDir).toAbsolutePath().normalize();
            Files.createDirectories(root);

            Path filePath = root.resolve(storeName);
            file.transferTo(filePath.toFile());

            FoodExtendFilePath extendFilePath = FoodExtendFilePath.builder()
                    .urlPath("/img/meal/")
                    .build();
            extendFilePathRepository.save(extendFilePath);

            FoodFileUpload upload = FoodFileUpload.builder()
                    .meal(meal)
                    .name(originalName)
                    .type(file.getContentType())
                    .reName(storeName)
                    .path("")
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
            if (f.getReName() == null) return;
            Path root = Paths.get(System.getProperty("user.dir"), uploadRootDir).toAbsolutePath().normalize();
            Path filePath = root.resolve(f.getReName());
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
