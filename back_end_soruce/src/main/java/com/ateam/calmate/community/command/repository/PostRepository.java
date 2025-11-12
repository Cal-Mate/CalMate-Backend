package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {}