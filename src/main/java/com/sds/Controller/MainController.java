package com.sds.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sds.Model.Banner;
import com.sds.Model.Blog;
import com.sds.Model.Consultation;
import com.sds.Model.ConsultationBooking;
import com.sds.Model.Course;
import com.sds.Model.CoursePurchase;
import com.sds.Model.EReport;
import com.sds.Model.EReportPurchase;
import com.sds.Model.Review;
import com.sds.Model.User;
import com.sds.Model.Video;
import com.sds.Service.EmailService;
import com.sds.Service.SDSservices;

import com.sds.config.FileUploadConfig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MainController {

    @Autowired
    private final FileUploadConfig fileUploadConfig;

    @Autowired
    private SDSservices sdsServices;

    @Autowired
    private EmailService emailService;
    


    MainController(FileUploadConfig fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
    }

    // ADMIN DASHBOARD CODE - COURSES
    
    @PostMapping("/savecourse")
    public ResponseEntity<String> saveCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile) {

        String uploadDir = System.getProperty("user.dir") + "/uploads/CourseLogo";
        String fileName = null;

        try {
            if (logoFile != null && !logoFile.isEmpty()) {
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                fileName = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                logoFile.transferTo(fileToSave);
            }

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setPrice(price);
            course.setLogoUrl(fileName);

            sdsServices.saveCourse(course);

            return ResponseEntity.ok("Course saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save course");
        }
    }

    @GetMapping("/getAllcourses")
    public List<Course> getAllCourses() {
        return sdsServices.getAllCourses();
    }

    @PutMapping("/UpdateCourse/{id}")
    public ResponseEntity<String> updateCourse(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile) {

        try {
            Course course = sdsServices.getCourseById(id);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }

            course.setTitle(title);
            course.setDescription(description);
            course.setPrice(price);

            if (logoFile != null && !logoFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/CourseLogo";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                logoFile.transferTo(fileToSave);

                // Delete old logo if exists
                if (course.getLogoUrl() != null) {
                    File oldFile = new File(uploadDir + "/" + course.getLogoUrl());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                course.setLogoUrl(fileName);
            }

            sdsServices.updateCourse(course);
            return ResponseEntity.ok("Course updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update course");
        }
    }

    @DeleteMapping("/deletecourse/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        try {
            Course course = sdsServices.getCourseById(id);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }

            // Delete logo file if it exists
            String logoFileName = course.getLogoUrl();
            if (logoFileName != null) {
                String filePath = System.getProperty("user.dir") + "/uploads/CourseLogo/" + logoFileName;
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }

            // Delete course from database
            sdsServices.deleteCourse(id);
            return ResponseEntity.ok("Course and associated logo deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete course");
        }
    }

    // ADMIN DASHBOARD CODE - EREPORT
    
    @PostMapping("/ereports")
    public ResponseEntity<?> createEReport(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("points") String points,
            @RequestParam("price") String price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            EReport eReport = new EReport();
            eReport.setTitle(title);
            eReport.setDescription(description);
            eReport.setPrice(price);
            
            // Split points by new line
            List<String> pointsList = Arrays.asList(points.split("\\r?\\n"));
            eReport.setPoints(pointsList);
            
            if (image != null && !image.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/EReportImages";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                eReport.setImageUrl(fileName);
            }
            
            EReport saved = sdsServices.saveEReport(eReport);
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating eReport: " + e.getMessage());
        }
    }

    @GetMapping("/ereports")
    public List<EReport> getAllEReports() {
        return sdsServices.getAllEReports();
    }

    @PutMapping("/ereports/{id}")
    public ResponseEntity<?> updateEReport(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("points") String points,
            @RequestParam("price") String price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        EReport existing = sdsServices.getEReportById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            existing.setTitle(title);
            existing.setDescription(description);
            
            // Convert points string to mutable list
            List<String> pointsList = new ArrayList<>(Arrays.asList(points.split("\\r?\\n")));
            existing.setPoints(pointsList);
            existing.setPrice(price);
            
            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (existing.getImageUrl() != null) {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/EReportImages";
                    File oldFile = new File(uploadDir + "/" + existing.getImageUrl());
                    if (oldFile.exists()) oldFile.delete();
                }
                
                // Save new image
                String uploadDir = System.getProperty("user.dir") + "/uploads/EReportImages";
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                existing.setImageUrl(fileName);
            }
            
            EReport updated = sdsServices.updateEReport(existing);
            return ResponseEntity.ok(updated);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating eReport: " + e.getMessage());
        }
    }

    @DeleteMapping("/ereports/{id}")
    public ResponseEntity<?> deleteEReport(@PathVariable Long id) {
        EReport eReport = sdsServices.getEReportById(id);
        if (eReport == null) {
            return ResponseEntity.notFound().build();
        }
        
        String fileName = eReport.getImageUrl();
        try {
            // Delete associated image file if exists
            if (fileName != null) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/EReportImages/" + fileName;
                File file = new File(uploadDir);
                if(file.exists()) {
                    file.delete();
                }
            }
            
            sdsServices.deleteEReport(id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting eReport: " + e.getMessage());
        }
    }
    
 // ADMIN DASHBOARD CODE - Consultation
    
    @PostMapping("/consultations")
    public ResponseEntity<?> createConsultation(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            Consultation consultation = new Consultation();
            consultation.setTitle(title);
            consultation.setDescription(description);
            consultation.setPrice(price);
            
            if (image != null && !image.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/ConsultationImages";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                consultation.setImageUrl(fileName);
            }
            
            Consultation saved = sdsServices.saveConsultation(consultation);
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating consultation: " + e.getMessage());
        }
    }

    @GetMapping("/consultations")
    public List<Consultation> getAllConsultations() {
        return sdsServices.getAllConsultations();
    }

    @PutMapping("/consultations/{id}")
    public ResponseEntity<?> updateConsultation(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Consultation existing = sdsServices.getConsultationById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            existing.setTitle(title);
            existing.setDescription(description);
            existing.setPrice(price);
            
            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (existing.getImageUrl() != null) {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/ConsultationImages";
                    File oldFile = new File(uploadDir + "/" + existing.getImageUrl());
                    if (oldFile.exists()) oldFile.delete();
                }
                
                // Save new image
                String uploadDir = System.getProperty("user.dir") + "/uploads/ConsultationImages";
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                existing.setImageUrl(fileName);
            }
            
            Consultation updated = sdsServices.updateConsultation(existing);
            return ResponseEntity.ok(updated);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating consultation: " + e.getMessage());
        }
    }

    @DeleteMapping("/consultations/{id}")
    public ResponseEntity<?> deleteConsultation(@PathVariable Long id) {
        Consultation consultation = sdsServices.getConsultationById(id);
        if (consultation == null) {
            return ResponseEntity.notFound().build();
        }
        
        String fileName = consultation.getImageUrl();
        try {
            // Delete associated image file if exists
            if (fileName != null) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/ConsultationImages/" + fileName;
                File file = new File(uploadDir);
                if(file.exists()) {
                    file.delete();
                }
            }
            
            sdsServices.deleteConsultation(id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting consultation: " + e.getMessage());
        }
    }
    
 // BANNER MANAGEMENT ENDPOINTS
    @PostMapping("/banners")
    public ResponseEntity<?> createBanner(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) {
        
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/BannerImages";
            String fileName = null;

            if (image != null && !image.isEmpty()) {
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
            }

            Banner banner = new Banner();
            banner.setName(name);
            banner.setImageUrl(fileName);

            Banner saved = sdsServices.saveBanner(banner);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating banner: " + e.getMessage());
        }
    }

    @GetMapping("/banners")
    public List<Banner> getAllBanners() {
        return sdsServices.getAllBanners();
    }

    @PutMapping("/banners/{id}")
    public ResponseEntity<?> updateBanner(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Banner existing = sdsServices.getBannerById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            existing.setName(name);

            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (existing.getImageUrl() != null) {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/BannerImages";
                    File oldFile = new File(uploadDir + "/" + existing.getImageUrl());
                    if (oldFile.exists()) oldFile.delete();
                }
                
                // Save new image
                String uploadDir = System.getProperty("user.dir") + "/uploads/BannerImages";
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                existing.setImageUrl(fileName);
            }
            
            Banner updated = sdsServices.updateBanner(existing);
            return ResponseEntity.ok(updated);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating banner: " + e.getMessage());
        }
    }

    @DeleteMapping("/banners/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        Banner banner = sdsServices.getBannerById(id);
        if (banner == null) {
            return ResponseEntity.notFound().build();
        }
        
        String fileName = banner.getImageUrl();
        try {
            // Delete associated image file if exists
            if (fileName != null) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/BannerImages/" + fileName;
                File file = new File(uploadDir);
                if(file.exists()) {
                    file.delete();
                }
            }
            
            sdsServices.deleteBanner(id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting banner: " + e.getMessage());
        }
    }
    
    
    
 // Blog Management Endpoints
    @PostMapping("/blogs")
    public ResponseEntity<?> createBlog(
            @RequestParam("title") String title,
           
            @RequestParam("content") String content,
           
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("author") String author,
            @RequestParam(value = "featured", required = false, defaultValue = "false") boolean featured) {
        
        try {
            Blog blog = new Blog();
            blog.setTitle(title);
            
            blog.setContent(content);
           
            blog.setDate(new Date());
            
            blog.setAuthor(author);
            blog.setFeatured(featured);

            if (image != null && !image.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/BlogImages";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();
                
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                blog.setImageUrl(fileName);
            }
            
            Blog saved = sdsServices.saveBlog(blog);
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating blog: " + e.getMessage());
        }
    }

    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {
        return sdsServices.getAllBlogs();
    }

    @GetMapping("/blogs/featured")
    public List<Blog> getFeaturedBlogs() {
        return sdsServices.getFeaturedBlogs();
    }

    

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = sdsServices.getBlogById(id);
        if (blog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blog);
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<?> updateBlog(
            @PathVariable Long id,
            @RequestParam("title") String title,
            
            @RequestParam("content") String content,
           
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("author") String author,
            @RequestParam(value = "featured", required = false) boolean featured) {
        
        Blog existing = sdsServices.getBlogById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            existing.setTitle(title);
            
            existing.setContent(content);
           
            existing.setAuthor(author);
            existing.setFeatured(featured);

            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (existing.getImageUrl() != null) {
                    String uploadDir = System.getProperty("user.dir") + "/uploads/BlogImages";
                    File oldFile = new File(uploadDir + "/" + existing.getImageUrl());
                    if (oldFile.exists()) oldFile.delete();
                }
                
                // Save new image
                String uploadDir = System.getProperty("user.dir") + "/uploads/BlogImages";
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File fileToSave = new File(uploadDir + "/" + fileName);
                image.transferTo(fileToSave);
                
                existing.setImageUrl(fileName);
            }
            
            Blog updated = sdsServices.updateBlog(existing);
            return ResponseEntity.ok(updated);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating blog: " + e.getMessage());
        }
    }

    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        Blog blog = sdsServices.getBlogById(id);
        if (blog == null) {
            return ResponseEntity.notFound().build();
        }
        
        String fileName = blog.getImageUrl();
        try {
            // Delete associated image file if exists
            if (fileName != null) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/BlogImages/" + fileName;
                File file = new File(uploadDir);
                if(file.exists()) {
                    file.delete();
                }
            }
            
            sdsServices.deleteBlog(id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting blog: " + e.getMessage());
        }
    }
    
    //VIDEO SAVE
    

 // Add these endpoints to your MainController

    @GetMapping("/getCourse")
    @ResponseBody
    public List<Course> getCourse() {
        return sdsServices.getAllCourses();
    }

    @GetMapping("/courses/{courseId}/videos")
    @ResponseBody
    public List<Video> getCourseVideos(@PathVariable Long courseId) {
        return sdsServices.getVideosByCourseId(courseId);
    }

    @PostMapping("/upload/{courseId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadVideo(
            @PathVariable Long courseId,
            @RequestParam("title") String title,
            @RequestParam("video") MultipartFile videoFile) {
        
        try {
            Video video = sdsServices.uploadVideo(courseId, title, videoFile);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Video uploaded successfully",
                "videoUrl", video.getVideoUrl()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "error",
                        "message", "Failed to upload video: " + e.getMessage()
                    ));
        }
    }
    
    @PutMapping("/videos/{videoId}")
    @ResponseBody
    public ResponseEntity<?> updateVideo(
            @PathVariable Long videoId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "video", required = false) MultipartFile videoFile) {
        
        try {
            Video existingVideo = sdsServices.getVideoById(videoId);
            if (existingVideo == null) {
                return ResponseEntity.notFound().build();
            }

            // Update title if provided
            if (title != null && !title.trim().isEmpty()) {
                existingVideo.setTitle(title);
            }

            // Update video file if provided
            if (videoFile != null && !videoFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/CourseVideos/";
                
                // Delete old video file
                if (existingVideo.getVideoUrl() != null) {
                    File oldFile = new File(uploadDir + existingVideo.getVideoUrl());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // Save new video file
                String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(videoFile.getInputStream(), filePath);
                
                existingVideo.setVideoUrl(fileName);
            }

            Video updatedVideo = sdsServices.updateVideo(existingVideo);
            return ResponseEntity.ok(updatedVideo);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update video file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating video: " + e.getMessage());
        }
    }

    @DeleteMapping("/videos/{videoId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteVideo(@PathVariable Long videoId) {
        try {
            sdsServices.deleteVideo(videoId);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Video deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "error",
                        "message", "Failed to delete video: " + e.getMessage()
                    ));
        }
    }
 
    
 // Add these endpoints to MainController.java

    @PostMapping("/purchase/initiate")
    public ResponseEntity<?> initiatePurchase(
            @RequestParam("email") String email,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "paymentProof", required = false) MultipartFile paymentProof,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "dob", required = false) String dob,
            @RequestParam(value = "birthTime", required = false) String birthTime,
            @RequestParam(value = "birthPlace", required = false) String birthPlace,
            @RequestParam(value = "password", required = false) String password) {

        try {
            // Check if course exists
            Course course = sdsServices.getCourseById(courseId);
            if (course == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Course not found"));
            }

            // Check if user exists
            User existingUser = sdsServices.findUserByEmail(email);
            boolean isNewUser = existingUser == null;

            // Validate new user data
            if (isNewUser) {
                if (fullName == null || phone == null || dob == null || 
                    birthTime == null || birthPlace == null || password == null) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "All fields are required for new users"));
                }
                
                if (password.length() < 8) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "Password must be at least 8 characters"));
                }
            }

            // Save payment proof
            String proofPath = null;
            if (paymentProof != null && !paymentProof.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/PaymentProofs/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + paymentProof.getOriginalFilename();
                File fileToSave = new File(uploadDir + fileName);
                paymentProof.transferTo(fileToSave);
                proofPath = fileName;
            }

            // Generate OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            
            // Create purchase request (status PENDING)
            CoursePurchase purchase = new CoursePurchase();
            purchase.setCourse(course);
            purchase.setStatus("PENDING");
            purchase.setPurchaseDate(new Date());
            purchase.setPaymentProofPath(proofPath);
            
            if (isNewUser) {
                // Create new user and save immediately (with temporary flag)
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setFullName(fullName);
                newUser.setPhone(phone);
                newUser.setPassword(password); // Note: Should be encrypted in production
                newUser.setBirthPlace(birthPlace);
                newUser.setBirthTime(birthTime);
                newUser.setTemporary(true); // Add this field to your User entity
                
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    newUser.setDob(sdf.parse(dob));
                } catch (ParseException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid date format for DOB (use yyyy-MM-dd)"));
                }
                
                // Save the user first
                User savedUser = sdsServices.saveUser(newUser);
                purchase.setUser(savedUser);
            } else {
                purchase.setUser(existingUser);
            }
            
            // Save purchase record
            CoursePurchase savedPurchase = sdsServices.savePurchase(purchase);
            
            // Send OTP to email
            emailService.sendEmail(email, "OTP for Course Purchase", 
                "Your OTP for purchasing " + course.getTitle() + " is: " + otp);

            return ResponseEntity.ok(Map.of(
                "otpRequired", true,
                "purchaseId", savedPurchase.getId(),
                "tempProofPath", proofPath,
                "message", "OTP sent to email"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error initiating purchase: " + e.getMessage()));
        }
    }

    @PostMapping("/purchase/verify-and-complete")
    public ResponseEntity<?> verifyAndCompletePurchase(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            @RequestParam("courseId") Long courseId,
            @RequestParam("purchaseId") Long purchaseId,
            @RequestParam(value = "tempProofPath", required = false) String tempProofPath) {

        try {
            // Validate OTP format
            if (otp == null || !otp.matches("\\d{6}")) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid OTP"));
            }

            // Get the purchase record
            CoursePurchase purchase = sdsServices.getPurchaseById(purchaseId);
            if (purchase == null || !"PENDING".equals(purchase.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid purchase request"));
            }

            // Check if user is temporary
            User user = purchase.getUser();
            if (user.isTemporary()) {
                // Mark user as permanent
                user.setTemporary(false);
                sdsServices.updateUser(user);
            }

            // Complete the purchase
            purchase.setStatus("COMPLETED");
            purchase.setVerified(false);
            purchase.setPurchaseDate(new Date());
            
            // Set expiry date
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            purchase.setExpiryDate(cal.getTime());
            
            // Update purchase record
            sdsServices.updatePurchase(purchase);

            // Send confirmation email
            emailService.sendPurchaseConfirmationEmail(user, purchase.getCourse());
         // Send notification email to admin
            emailService.sendAdminPurchaseNotification(user, purchase.getCourse(), purchase);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Purchase completed successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error completing purchase: " + e.getMessage()));
        }
    }
   
 // Get all purchases for admin
    @GetMapping("/admin/course-purchases")
    public ResponseEntity<?> getAllPurchases(
            @RequestParam(required = false) String status) {
        try {
            List<CoursePurchase> purchases;
            if (status != null && !status.isEmpty()) {
                purchases = sdsServices.getPurchasesByStatus(status);
            } else {
                purchases = sdsServices.getAllPurchases();
            }
            
            // Convert to DTOs for frontend
            List<Map<String, Object>> result = purchases.stream().map(purchase -> {
                Map<String, Object> dto = new HashMap<>();
                dto.put("id", purchase.getId());
                dto.put("userName", purchase.getUser().getFullName());
                dto.put("userEmail", purchase.getUser().getEmail());
                dto.put("userPhone", purchase.getUser().getPhone());
                dto.put("course", purchase.getCourse());
                dto.put("purchaseDate", purchase.getPurchaseDate());
                dto.put("expiryDate", purchase.getExpiryDate());
                dto.put("status", purchase.getStatus());
                dto.put("verified", purchase.isVerified());
                dto.put("payment", Map.of(
                    "amount", purchase.getCourse().getPrice(),
                    "paymentMethod", "UPI",
                    "screenshotPath", purchase.getPaymentProofPath()
                ));
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching purchases: " + e.getMessage()));
        }
    }

    // Approve purchase
    @PutMapping("/admin/course-purchases/{id}/approve")
    public ResponseEntity<?> approvePurchase(@PathVariable Long id) {
        try {
            CoursePurchase purchase = sdsServices.getPurchaseById(id);
            if (purchase == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Purchase not found"));
            }
            
            purchase.setStatus("APPROVED");
            purchase.setVerified(true);
            sdsServices.updatePurchase(purchase);
            
            // Send approval email to user
            emailService.sendEmail(
                purchase.getUser().getEmail(),
                "Your Course Purchase Approved",
                "Dear " + purchase.getUser().getFullName() + ",\n\n" +
                "Your purchase of " + purchase.getCourse().getTitle() + " has been approved.\n" +
                "You can now access the course content.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Purchase approved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error approving purchase: " + e.getMessage()));
        }
    }

    // Reject purchase
    @PutMapping("/admin/course-purchases/{id}/reject")
    public ResponseEntity<?> rejectPurchase(@PathVariable Long id) {
        try {
            CoursePurchase purchase = sdsServices.getPurchaseById(id);
            if (purchase == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Purchase not found"));
            }
            
            purchase.setStatus("REJECTED");
            purchase.setVerified(false);
            sdsServices.updatePurchase(purchase);
            
            // Send rejection email to user
            emailService.sendEmail(
                purchase.getUser().getEmail(),
                "Your Course Purchase Rejected",
                "Dear " + purchase.getUser().getFullName() + ",\n\n" +
                "Your purchase of " + purchase.getCourse().getTitle() + " has been rejected.\n" +
                "If you believe this is an error, please contact support.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Purchase rejected successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error rejecting purchase: " + e.getMessage()));
        }
    }
    
    
    // FRONTEND DATA SHOW BACKEND CODE
    
    @GetMapping("/getCourses")
    public List<Course> getALLCourses() {
        return sdsServices.getAllCourses();
    }
    
    @GetMapping("/Allereports")
    public List<EReport> getAllEReport() {
        return sdsServices.getAllEReports();
    }
    
    @GetMapping("/Allconsultations")
    public List<Consultation> AllConsultations() {
        return sdsServices.getAllConsultations();
    }
    
    // USER DASHBOARD CODE 
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        User user = sdsServices.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
        
        if (!password.equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
        
        request.getSession().setAttribute("user", user);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole()); // Add role to response
        response.put("message", "Login successful");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String email) {
        User user = sdsServices.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.ok(Map.of("message", "OTP sent to email if it exists"));
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Set expiry to 10 minutes from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);

        user.setOtp(otp);
        user.setOtpExpiry(calendar.getTime());
        sdsServices.updateUser(user);

        // Send OTP via email
        emailService.sendEmail(email, "Password Reset OTP",
                "Your OTP is: " + otp + "\nValid for 10 minutes");

        return ResponseEntity.ok(Map.of("message", "OTP sent to email if it exists"));
    }

    // ============================
    // 2. Verify OTP
    // ============================
    @PostMapping("/auth/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        User user = sdsServices.findUserByEmail(email);
        if (user == null || !otp.equals(user.getOtp())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid OTP"));
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "OTP expired"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    // ============================
    // 3. Reset Password
    // ============================
    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        // Validate required fields
        if (email == null || otp == null || newPassword == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email, OTP and new password are required"));
        }

        User user = sdsServices.findUserByEmail(email);
        if (user == null || !otp.equals(user.getOtp())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid OTP"));
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "OTP expired"));
        }

        // Update password (consider hashing here)
        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        sdsServices.updateUser(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
    
 // User Dashboard Endpoints
    @GetMapping("/auth/user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
    	User user1 = (User) session.getAttribute("user");
        User user = sdsServices.findUserByEmail(user1.getEmail());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Create response DTO
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("dob", user.getDob());
        response.put("birthPlace", user.getBirthPlace());
        response.put("birthTime", user.getBirthTime());
        response.put("profileImage", user.getProfileImage());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/courses")
    public ResponseEntity<?> getUserCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<CoursePurchase> purchases = sdsServices.getPurchasesByUser(user.getId())
            .stream()
            .filter(p -> "APPROVED".equals(p.getStatus()))
            .collect(Collectors.toList());
        
        List<Map<String, Object>> response = purchases.stream().map(purchase -> {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("id", purchase.getId());
            courseData.put("courseId", purchase.getCourse().getId());
            courseData.put("title", purchase.getCourse().getTitle());
            courseData.put("description", purchase.getCourse().getDescription());
            courseData.put("price", purchase.getCourse().getPrice());
            courseData.put("logoUrl", purchase.getCourse().getLogoUrl());
            courseData.put("status", purchase.getStatus());
            courseData.put("purchaseDate", purchase.getPurchaseDate());
            return courseData;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/user/pending-courses")
    public ResponseEntity<?> getUserPendingCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        List<CoursePurchase> purchases = sdsServices.getPurchasesByUser(user.getId())
            .stream()
            .filter(p -> !"APPROVED".equals(p.getStatus()))
            .collect(Collectors.toList());
        
        List<Map<String, Object>> response = purchases.stream().map(purchase -> {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("id", purchase.getId());
            courseData.put("courseId", purchase.getCourse().getId());
            courseData.put("title", purchase.getCourse().getTitle());
            courseData.put("description", purchase.getCourse().getDescription());
            courseData.put("price", purchase.getCourse().getPrice());
            courseData.put("logoUrl", purchase.getCourse().getLogoUrl());
            courseData.put("status", purchase.getStatus());
            courseData.put("purchaseDate", purchase.getPurchaseDate());
            courseData.put("paymentProofPath", purchase.getPaymentProofPath());
            return courseData;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/user/profile")
    public ResponseEntity<?> updateProfile(
    		HttpSession session,
            @RequestBody Map<String, String> profileData) {
        
    	User user1 = (User) session.getAttribute("user");
        User user = sdsServices.findUserByEmail(user1.getEmail());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Update user fields
        user.setFullName(profileData.get("fullName"));
        user.setPhone(profileData.get("phone"));
        
        try {
            if (profileData.get("dob") != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                user.setDob(sdf.parse(profileData.get("dob")));
            }
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
        
        User updatedUser = sdsServices.updateUser(user);
        
        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("id", updatedUser.getId());
        response.put("fullName", updatedUser.getFullName());
        response.put("email", updatedUser.getEmail());
        response.put("phone", updatedUser.getPhone());
        response.put("dob", updatedUser.getDob());
        response.put("profileImage", updatedUser.getProfileImage());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/profile/image")
    public ResponseEntity<?> updateProfileImage(
    		HttpSession session,
            @RequestParam("profileImage") MultipartFile image) {
        
    	User user1 = (User) session.getAttribute("user");
        User user = sdsServices.findUserByEmail(user1.getEmail());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/profile/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            
            // Delete old image if exists
            if (user.getProfileImage() != null) {
                File oldFile = new File(uploadDir + user.getProfileImage());
                if (oldFile.exists()) oldFile.delete();
            }
            
            // Save new image
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            File fileToSave = new File(uploadDir + fileName);
            image.transferTo(fileToSave);
            
            user.setProfileImage(fileName);
            User updatedUser = sdsServices.updateUser(user);
            
            return ResponseEntity.ok(Map.of(
                "profileImage", updatedUser.getProfileImage()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }
    
    @GetMapping("/course/{courseId}/videos")
    @ResponseBody
    public ResponseEntity<?> getCourseVideo(
        @PathVariable Long courseId,
        HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Check if user has access to this course
        boolean hasAccess = sdsServices.getPurchasesByUser(user.getId()).stream()
            .anyMatch(p -> 
                p.getCourse().getId().equals(courseId) && 
                "APPROVED".equals(p.getStatus()));
        
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You don't have access to this course");
        }
        
        List<Video> videos = sdsServices.getVideosByCourseId(courseId);
        return ResponseEntity.ok(videos);
    }
    
    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = sdsServices.getCourseById(id);
            if (course == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching course: " + e.getMessage());
        }
    }
    
    @GetMapping("/stream-video/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable Long videoId,
            HttpSession session) throws IOException {
        
        // 1. Authentication check
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // 2. Get video from database
        Video video = sdsServices.getVideoById(videoId);
        if (video == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 3. Verify course access
        boolean hasAccess = sdsServices.getPurchasesByUser(user.getId()).stream()
            .anyMatch(p -> 
                p.getCourse().getId().equals(video.getCourse().getId()) && 
                "APPROVED".equals(p.getStatus()));
        
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // 4. Construct file path
        String uploadDir = System.getProperty("user.dir") + "/uploads/CourseVideos/";
        Path videoPath = Paths.get(uploadDir).resolve(video.getVideoUrl()).normalize();
        Resource resource = new UrlResource(videoPath.toUri());
        
        // 5. Verify file exists
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        // 6. Determine content type
        String contentType = determineContentType(video.getVideoUrl());
        
        // 7. Return video stream
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "inline; filename=\"" + video.getVideoUrl() + "\"")
                .body(resource);
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".mp4")) {
            return "video/mp4";
        } else if (filename.endsWith(".webm")) {
            return "video/webm";
        } else if (filename.endsWith(".ogg")) {
            return "video/ogg";
        }
        return "video/mp4"; // default
    }
    
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        // Clear authentication cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }
    
    //Cousaltation Booking 
    
    @PostMapping("/consultations/book")
    public ResponseEntity<?> bookConsultation(
            @RequestParam("consultationId") Long consultationId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("dob") String dob,
            @RequestParam("time") String time,
            @RequestParam("placeOfBirth") String placeOfBirth,
            @RequestParam("questions") String questions,
            @RequestParam("paymentProof") MultipartFile paymentProof) {

        try {
            // Get consultation
            Consultation consultation = sdsServices.getConsultationById(consultationId);
            if (consultation == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Consultation not found"));
            }

            // Save payment proof
            String paymentProofUrl = null;
            if (paymentProof != null && !paymentProof.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/consultation_payments/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + paymentProof.getOriginalFilename();
                File fileToSave = new File(uploadDir + fileName);
                paymentProof.transferTo(fileToSave);
                paymentProofUrl = fileName;
            }

            // Create booking
            ConsultationBooking booking = new ConsultationBooking();
            booking.setConsultation(consultation);
            booking.setName(name);
            booking.setEmail(email);
            booking.setPhone(phone);
            booking.setDob(dob);
            booking.setTime(time);
            booking.setPlaceOfBirth(placeOfBirth);
            booking.setQuestions(questions);
            booking.setPaymentProofUrl(paymentProofUrl);
            booking.setStatus("PENDING");

            ConsultationBooking savedBooking = sdsServices.saveBooking(booking);

            // Send WhatsApp message (as in your frontend)
            String whatsappMessage = "New Consultation Booking:\n\n" +
                    "*Service:* " + consultation.getTitle() + "\n" +
                    "*Price:* " + consultation.getPrice() + "\n\n" +
                    "*Client Details:*\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Phone: " + phone + "\n" +
                    "DOB: " + dob + "\n" +
                    "Time: " + time + "\n" +
                    "Place of Birth: " + placeOfBirth + "\n\n" +
                    "*Questions:*\n" + questions + "\n\n" +
                    "Booking ID: " + savedBooking.getId();

            // URL encode the message
            String encodedMessage = URLEncoder.encode(whatsappMessage, StandardCharsets.UTF_8.toString());
            String whatsappUrl = "https://wa.me/919284389450?text=" + encodedMessage;

            // Return response with booking ID and WhatsApp URL
            return ResponseEntity.ok(Map.of(
                "success", true,
                "bookingId", savedBooking.getId(),
                "whatsappUrl", whatsappUrl,
                "message", "Booking created successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating booking: " + e.getMessage()));
        }
    }

    @GetMapping("/consultations/bookings")
    public ResponseEntity<?> getAllConsultationBookings(
            @RequestParam(required = false) String status) {
        try {
            List<ConsultationBooking> bookings;
            if (status != null && !status.isEmpty()) {
                bookings = sdsServices.getBookingsByStatus(status);
            } else {
                bookings = sdsServices.getAllBookings();
            }
            
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching bookings: " + e.getMessage()));
        }
    }
    
    @PutMapping("/consultations/bookings/{id}/approve")
    public ResponseEntity<?> approveBooking(@PathVariable Long id) {
        try {
            ConsultationBooking booking = sdsServices.getBookingById(id);
            if (booking == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Booking not found"));
            }
            
            booking.setStatus("APPROVED");
            sdsServices.updateBooking(booking);
            
            // Send approval email
            emailService.sendEmail(
                booking.getEmail(),
                "Your Consultation Booking Approved",
                "Dear " + booking.getName() + ",\n\n" +
                "Your booking for " + booking.getConsultation().getTitle() + " has been approved.\n" +
                "We will contact you shortly to schedule your consultation.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Booking approved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error approving booking: " + e.getMessage()));
        }
    }

    @PutMapping("/consultations/bookings/{id}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id) {
        try {
            ConsultationBooking booking = sdsServices.getBookingById(id);
            if (booking == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Booking not found"));
            }
            
            booking.setStatus("REJECTED");
            sdsServices.updateBooking(booking);
            
            // Send rejection email
            emailService.sendEmail(
                booking.getEmail(),
                "Your Consultation Booking Rejected",
                "Dear " + booking.getName() + ",\n\n" +
                "Your booking for " + booking.getConsultation().getTitle() + " has been rejected.\n" +
                "If you believe this is an error, please contact support.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Booking rejected successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error rejecting booking: " + e.getMessage()));
        }
    }
    
    //E-Report Purches
    
    @PostMapping("/ereports/purchase")
    public ResponseEntity<?> purchaseEReport(
            @RequestParam("eReportId") Long eReportId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("dob") String dob,
            @RequestParam("birthTime") String birthTime,
            @RequestParam("birthPlace") String birthPlace,
            @RequestParam("specificQuestions") String specificQuestions,
            @RequestParam("paymentProof") MultipartFile paymentProof,
            @RequestParam("amountPaid") Double amountPaid) {

        try {
            // Get eReport
            EReport eReport = sdsServices.getEReportById(eReportId);
            if (eReport == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "E-Report not found"));
            }

            // Save payment proof
            String paymentProofUrl = null;
            if (paymentProof != null && !paymentProof.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/ereport_payments/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + paymentProof.getOriginalFilename();
                File fileToSave = new File(uploadDir + fileName);
                paymentProof.transferTo(fileToSave);
                paymentProofUrl = fileName;
            }

            // Create purchase
            EReportPurchase purchase = new EReportPurchase();
            purchase.setEReport(eReport);
            purchase.setName(name);
            purchase.setEmail(email);
            purchase.setPhone(phone);
            purchase.setDob(dob);
            purchase.setBirthTime(birthTime);
            purchase.setBirthPlace(birthPlace);
            purchase.setSpecificQuestions(specificQuestions);
            purchase.setPaymentProofUrl(paymentProofUrl);
            purchase.setAmountPaid(amountPaid);
            purchase.setStatus("PENDING");

            EReportPurchase savedPurchase = sdsServices.saveEReportPurchase(purchase);

            // Send WhatsApp message
            String whatsappMessage = "New E-Report Purchase:\n\n" +
                    "*Report:* " + eReport.getTitle() + "\n" +
                    "*Price:* ₹" + amountPaid + "\n\n" +
                    "*Customer Details:*\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Phone: " + phone + "\n" +
                    "DOB: " + dob + "\n" +
                    "Birth Time: " + birthTime + "\n" +
                    "Birth Place: " + birthPlace + "\n\n" +
                    "*Questions:*\n" + specificQuestions + "\n\n" +
                    "Purchase ID: " + savedPurchase.getId();

            String encodedMessage = URLEncoder.encode(whatsappMessage, StandardCharsets.UTF_8.toString());
            String whatsappUrl = "https://wa.me/919284389450?text=" + encodedMessage;

            return ResponseEntity.ok(Map.of(
                "success", true,
                "purchaseId", savedPurchase.getId(),
                "whatsappUrl", whatsappUrl,
                "message", "Purchase created successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating purchase: " + e.getMessage()));
        }
    }

    @GetMapping("/ereports/purchases")
    public ResponseEntity<?> getAllEReportPurchases(
            @RequestParam(required = false) String status) {
        try {
            List<EReportPurchase> purchases;
            if (status != null && !status.isEmpty()) {
                purchases = sdsServices.getEReportPurchasesByStatus(status);
            } else {
                purchases = sdsServices.getAllEReportPurchases();
            }
            
            // Log the first purchase to verify data
            if (!purchases.isEmpty()) {
                System.out.println("First purchase data: " + purchases.get(0));
                if (purchases.get(0).getEReport() != null) {
                    System.out.println("First purchase E-Report title: " + 
                        purchases.get(0).getEReport().getTitle());
                }
            }
            
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching purchases: " + e.getMessage()));
        }
    }

    @PutMapping("/ereports/purchases/{id}/approve")
    public ResponseEntity<?> approveEReportPurchase(@PathVariable Long id) {
        try {
            EReportPurchase purchase = sdsServices.getEReportPurchaseById(id);
            if (purchase == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Purchase not found"));
            }
            
            purchase.setStatus("APPROVED");
            sdsServices.updateEReportPurchase(purchase);
            
            // Send approval email
            emailService.sendEmail(
                purchase.getEmail(),
                "Your E-Report Purchase Approved",
                "Dear " + purchase.getName() + ",\n\n" +
                "Your purchase of " + purchase.getEReport().getTitle() + " has been approved.\n" +
                "You will receive your report shortly.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Purchase approved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error approving purchase: " + e.getMessage()));
        }
    }

    @PutMapping("/ereports/purchases/{id}/reject")
    public ResponseEntity<?> rejectEReportPurchase(@PathVariable Long id) {
        try {
            EReportPurchase purchase = sdsServices.getEReportPurchaseById(id);
            if (purchase == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Purchase not found"));
            }
            
            purchase.setStatus("REJECTED");
            sdsServices.updateEReportPurchase(purchase);
            
            // Send rejection email
            emailService.sendEmail(
                purchase.getEmail(),
                "Your E-Report Purchase Rejected",
                "Dear " + purchase.getName() + ",\n\n" +
                "Your purchase of " + purchase.getEReport().getTitle() + " has been rejected.\n" +
                "If you believe this is an error, please contact support.\n\n" +
                "Thank you,\nSacred Numerology Team"
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Purchase rejected successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error rejecting purchase: " + e.getMessage()));
        }
    }
    
 // Add these endpoints to MainController
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            review.setCreatedAt(new Date());
            Review savedReview = sdsServices.saveReview(review);
            return ResponseEntity.ok(savedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error submitting review: " + e.getMessage());
        }
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return sdsServices.getAllReviews();
    }

    // Admin endpoint
    @DeleteMapping("/admin/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            sdsServices.deleteReview(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting review: " + e.getMessage());
        }
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return sdsServices.getAllUsers();
    }
    
    @GetMapping("/admin/dashboard-stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            // Get counts
            long totalUsers = sdsServices.getTotalUsers();
            long totalCoursePurchases = sdsServices.getTotalCoursePurchases();
            long totalConsultationBookings = sdsServices.getTotalConsultationBookings();
            long totalEReportPurchases = sdsServices.getTotalEReportPurchases();
            
            // Get revenue data
            Map<String, Double> revenueStats = sdsServices.getRevenueStats();
            
            // Get time-based counts
            Map<String, Map<String, Long>> timeBasedStats = sdsServices.getTimeBasedStats();
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("totalUsers", totalUsers);
            response.put("totalCoursePurchases", totalCoursePurchases);
            response.put("totalConsultationBookings", totalConsultationBookings);
            response.put("totalEReportPurchases", totalEReportPurchases);
            response.put("revenueStats", revenueStats);
            response.put("timeBasedStats", timeBasedStats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching dashboard stats: " + e.getMessage()));
        }
    }

    
}