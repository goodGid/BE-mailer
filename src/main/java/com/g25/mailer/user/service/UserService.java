package com.g25.mailer.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.g25.mailer.user.dto.AddUserRequest;
import com.g25.mailer.user.entity.User;
import com.g25.mailer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String PROFILE_IMAGE_DIR = "uploads/profile_images/";

    public Long save(AddUserRequest dto) {
        return userRepository.save(
                User.builder()
                    .email(dto.getEmail())
                    .password(bCryptPasswordEncoder.encode(dto.getPassword())) //패스워드 암호화 : 인코딩용 빈을 사용하여 암호화한후 저장
                    .build()).getId();
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 디렉토리 없으면 생성
        File uploadDir = new File(PROFILE_IMAGE_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 랜덤 UUID + 원본 확장자로 저장
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + extension;

        File destinationFile = new File(PROFILE_IMAGE_DIR + newFileName);
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image", e);
        }

        // 기존 이미지 삭제 (새 이미지로 교체할 경우)
        if (user.getProfileImageUrl() != null) {
            File oldFile = new File(user.getProfileImageUrl());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        // DB에 새 이미지 경로 저장
        user.setProfileImageUrl(PROFILE_IMAGE_DIR + newFileName);
        userRepository.save(user);

        return user.getProfileImageUrl();
    }

    public void deleteProfileImage(Long userId) {
    }


//    /**
//     * User CRUD
//     */
//    // CREATE: 사용자 생성
//    @Transactional
//    public UserRes createUser(UserCreateReq request) {
//        if (userRepository.existsByLoginId(request.getLoginId())) {
//            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
//        }
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
//        }
//
//        User user = User.builder()
//                .loginId(request.getLoginId())
//                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
//                .name(request.getName())
//                .email(request.getEmail())
//                .contact(request.getContact())
//                .role(User.Role.USER)
//                .status(User.Status.ACTIVE)
//                .build();
//
//
//        User savedUser = userRepository.save(user);
//        return UserRes.fromEntity(savedUser);
//    }
//


//
//    // READ: 특정 사용자 조회
//    @Transactional(readOnly = true)
//    public UserRes getUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//        return UserRes.fromEntity(user);
//    }
//
//    // UPDATE: 사용자 정보 수정
//    @Transactional
//    public UserRes updateUser(Long id, UserUpdateReq request) {
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        existingUser.setName(request.getName());
//        existingUser.setEmail(request.getEmail());
//        existingUser.setContact(request.getContact());
//        existingUser.setRole(request.getRole());
//        existingUser.setStatus(request.getStatus());
//
//        User updatedUser = userRepository.save(existingUser);
//        return UserRes.fromEntity(updatedUser);
//    }
//
//    // DELETE: 사용자 삭제
//    @Transactional
//    public void deleteUser(Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
//        }
//        userRepository.deleteById(id);
//    }

}
