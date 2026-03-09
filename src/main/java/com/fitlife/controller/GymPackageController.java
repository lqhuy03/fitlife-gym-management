package com.fitlife.controller;

import com.fitlife.dto.ApiResponse;
import com.fitlife.dto.GymPackageCreationRequest;
import com.fitlife.dto.GymPackageResponse;
import com.fitlife.dto.PageResponse;
import com.fitlife.service.GymPackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages") // Đổi lại thành chuẩn chung
@RequiredArgsConstructor
public class GymPackageController {

    private final GymPackageService gymPackageService;

    // --- API MỚI: XEM DANH SÁCH GÓI TẬP (AI CŨNG XEM ĐƯỢC) ---
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<PageResponse<GymPackageResponse>>> getAllPackages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<GymPackageResponse> result = gymPackageService.getAllPackages(page, size, sortBy, sortDir, keyword);

        return ResponseEntity.ok(ApiResponse.<PageResponse<GymPackageResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách gói tập thành công")
                .data(result)
                .build());
    }

    // --- API CŨ: TẠO GÓI TẬP (CHỈ ADMIN MỚI ĐƯỢC TẠO) ---
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<GymPackageResponse>> createPackage(@Valid @RequestBody GymPackageCreationRequest request) {
        GymPackageResponse result = gymPackageService.createPackage(request);

        ApiResponse<GymPackageResponse> response = ApiResponse.<GymPackageResponse>builder()
                .code(HttpStatus.CREATED.value()) // 201
                .message("Package created successfully")
                .data(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}