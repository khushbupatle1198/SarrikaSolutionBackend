package com.sds.Service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sds.Dao.SDSDao;
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

@Service
public class SDSserviceImpl implements SDSservices {

    @Autowired
    private SDSDao dao;
    
    @Autowired private EmailService emailService;
    
    // Course Methods
    public void saveCourse(Course course) {
        dao.saveCourse(course);
    }

    public List<Course> getAllCourses() {
        return dao.getAllCourses();
    }

    public Course getCourseById(Long id) {
        return dao.getCourseById(id);
    }

    public void updateCourse(Course course) {
        dao.updateCourse(course);
    }

    public void deleteCourse(Long id) {
        dao.deleteCourse(id);
    }

 // EReport Methods
    @Override
    public EReport saveEReport(EReport eReport) {
        return dao.saveEReport(eReport);
    }

    @Override
    public List<EReport> getAllEReports() {
        return dao.getAllEReports();
    }

    @Override
    public EReport getEReportById(Long id) {
        return dao.getEReportById(id);
    }

    @Override
    public EReport updateEReport(EReport eReport) {
        // Ensure points list is mutable
        if (eReport.getPoints() != null) {
            eReport.setPoints(new ArrayList<>(eReport.getPoints()));
        }
        return dao.updateEReport(eReport);
    }

    @Override
    public void deleteEReport(Long id) {
        dao.deleteEReport(id);
    }
    
    
    //CONSOLTATION
    
    @Override
    public List<Consultation> getAllConsultations() {
        return dao.getAllConsultations();
    }

    @Override
    public Consultation getConsultationById(Long id) {
        return dao.getConsultationById(id);
    }

    @Override
    public Consultation saveConsultation(Consultation consultation) {
        return dao.saveConsultation(consultation);
    }

    @Override
    public Consultation updateConsultation(Consultation consultation) {
        return dao.updateConsultation(consultation);
    }

    @Override
    public void deleteConsultation(Long id) {
        dao.deleteConsultation(id);
    }
    
 
 // Banner Methods
    @Override
    public Banner saveBanner(Banner banner) {
        return dao.saveBanner(banner);
    }

    @Override
    public List<Banner> getAllBanners() {
        return dao.getAllBanners();
    }

    @Override
    public Banner getBannerById(Long id) {
        return dao.getBannerById(id);
    }

    @Override
    public Banner updateBanner(Banner banner) {
        return dao.updateBanner(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        dao.deleteBanner(id);
    }
    
    
 // BLOGS
    @Override
    public List<Blog> getAllBlogs() {
        return dao.getAllBlogs();
    }

    @Override
    public Blog getBlogById(Long id) {
        return dao.getBlogById(id);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        return dao.saveBlog(blog);
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return dao.updateBlog(blog);
    }

    @Override
    public void deleteBlog(Long id) {
        dao.deleteBlog(id);
    }

    @Override
    public List<Blog> getFeaturedBlogs() {
        return dao.getFeaturedBlogs();
    }

    @Override
    public List<Blog> getBlogsByCategory(String category) {
        return dao.getBlogsByCategory(category);
    }
    
    
 // Add these methods to your SDSserviceImpl class
    private static final String VIDEO_UPLOAD_DIR = "uploads/CourseVideos/";

    @Override
    public Video uploadVideo(Long courseId, String title, MultipartFile videoFile) throws IOException {
        // Create upload directory if it doesn't exist
        String uploadDir = System.getProperty("user.dir") + "/uploads/CourseVideos/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Generate unique filename
        String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        
        // Save file
        Files.copy(videoFile.getInputStream(), filePath);

        // Get course
        Course course = getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        // Create and save video
        Video video = new Video();
        video.setTitle(title);
        video.setVideoUrl(fileName);
        video.setCourse(course);
        
        return dao.saveVideo(video);
    }

    @Override
    public List<Video> getVideosByCourseId(Long courseId) {
        return dao.getVideosByCourseId(courseId);
    }

    @Override
    public void deleteVideo(Long videoId) {
        Video video = dao.getVideoById(videoId);
        if (video != null) {
            // Delete file
            File file = new File(VIDEO_UPLOAD_DIR + video.getVideoUrl());
            if (file.exists()) {
                file.delete();
            }
            // Delete from database
            dao.deleteVideo(videoId);
        }
    }

    @Override
    public Video getVideoById(Long videoId) {
        return dao.getVideoById(videoId);
    }
    
    @Override
    public Video updateVideo(Video video) {
        return dao.updateVideo(video);
    }
       
 // Implement in SDSserviceImpl.java:
    @Override
    public User findUserByEmail(String email) {
        return dao.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return dao.saveUser(user);
    }

    @Override
    public CoursePurchase savePurchase(CoursePurchase purchase) {
        return dao.savePurchase(purchase);
    }

    @Override
    public CoursePurchase getPurchaseById(Long id) {
        return dao.getPurchaseById(id);
    }

    @Override
    public CoursePurchase updatePurchase(CoursePurchase purchase) {
        return dao.updatePurchase(purchase);
    }
    
    @Override
    public User updateUser(User user) {
        return dao.updateUser(user);
    }
    
    @Override
    public List<CoursePurchase> getAllPurchases() {
        return dao.getAllPurchases();
    }

    @Override
    public List<CoursePurchase> getPurchasesByStatus(String status) {
        return dao.getPurchasesByStatus(status);
    }

    @Override
    public User getUserById(Long id) {
        return dao.getUserById(id);
    }
    
    @Override
    public User findUserByResetToken(String token) {
        return dao.findUserByResetToken(token);
    }

	@Override
	public List<CoursePurchase> getPurchasesByUser(Long userId) {
		// TODO Auto-generated method stub
		return dao.getPurchasesByUser(userId);
	}

	@Override
	public ConsultationBooking saveBooking(ConsultationBooking booking) {
		// TODO Auto-generated method stub
		return dao.saveBooking(booking);
	}

	@Override
	public List<ConsultationBooking> getBookingsByStatus(String status) {
		// TODO Auto-generated method stub
		return dao.getBookingsByStatus(status);
	}

	@Override
	public List<ConsultationBooking> getAllBookings() {
		// TODO Auto-generated method stub
		return dao.getAllBookings();
	}

	@Override
	public ConsultationBooking getBookingById(Long id) {
		// TODO Auto-generated method stub
		return dao.getBookingById(id);
	}

	@Override
	
	public void updateBooking(ConsultationBooking booking) {
	    dao.updateBooking(booking);
	}
   
	
	@Override
	public EReportPurchase saveEReportPurchase(EReportPurchase purchase) {
		
	    return dao.saveEReportPurchase(purchase);
	}

	@Override
	public List<EReportPurchase> getEReportPurchasesByStatus(String status) {
	    return dao.getEReportPurchasesByStatus(status);
	}

	@Override
	public List<EReportPurchase> getAllEReportPurchases() {
	    return dao.getAllEReportPurchases();
	}

	@Override
	public EReportPurchase getEReportPurchaseById(Long id) {
	    return dao.getEReportPurchaseById(id);
	}

	@Override
	public void updateEReportPurchase(EReportPurchase purchase) {
	    dao.updateEReportPurchase(purchase);
	}

	@Override
	public Review saveReview(Review review) {
		// TODO Auto-generated method stub
		return dao.saveReview(review);
	}

	@Override
	public List<Review> getAllReviews() {
		// TODO Auto-generated method stub
		return dao.getAllReviews();
	}

	@Override
	public void deleteReview(Long id) {
		// TODO Auto-generated method stub
		dao.deleteReview(id);
	}
	
	@Override
	public List<User> getAllUsers() {
	    return dao.getAllUsers();
	}
	
	@Override
	public long getTotalUsers() {
	    return dao.getTotalUsers();
	}

	@Override
	public long getTotalCoursePurchases() {
	    return dao.getTotalCoursePurchases();
	}

	@Override
	public long getTotalConsultationBookings() {
	    return dao.getTotalConsultationBookings();
	}

	@Override
	public long getTotalEReportPurchases() {
	    return dao.getTotalEReportPurchases();
	}

	@Override
	public Map<String, Double> getRevenueStats() {
	    Map<String, Double> revenueStats = new HashMap<>();
	    
	    // Today's revenue
	    Double todayRevenue = dao.getRevenueForPeriod("today");
	    revenueStats.put("today", todayRevenue != null ? todayRevenue : 0.0);
	    
	    // Yesterday's revenue
	    Double yesterdayRevenue = dao.getRevenueForPeriod("yesterday");
	    revenueStats.put("yesterday", yesterdayRevenue != null ? yesterdayRevenue : 0.0);
	    
	    // This week's revenue
	    Double weekRevenue = dao.getRevenueForPeriod("week");
	    revenueStats.put("week", weekRevenue != null ? weekRevenue : 0.0);
	    
	    // This month's revenue
	    Double monthRevenue = dao.getRevenueForPeriod("month");
	    revenueStats.put("month", monthRevenue != null ? monthRevenue : 0.0);
	    
	    // This year's revenue
	    Double yearRevenue = dao.getRevenueForPeriod("year");
	    revenueStats.put("year", yearRevenue != null ? yearRevenue : 0.0);
	    
	    // Total revenue
	    Double totalRevenue = dao.getRevenueForPeriod("all");
	    revenueStats.put("total", totalRevenue != null ? totalRevenue : 0.0);
	    
	    return revenueStats;
	}

	@Override
	public Map<String, Map<String, Long>> getTimeBasedStats() {
	    Map<String, Map<String, Long>> stats = new HashMap<>();
	    
	    // Today's counts
	    stats.put("today", dao.getCountsForPeriod("today"));
	    
	    // Yesterday's counts
	    stats.put("yesterday", dao.getCountsForPeriod("yesterday"));
	    
	    // This week's counts
	    stats.put("week", dao.getCountsForPeriod("week"));
	    
	    // This month's counts
	    stats.put("month", dao.getCountsForPeriod("month"));
	    
	    // This year's counts
	    stats.put("year", dao.getCountsForPeriod("year"));
	    
	    return stats;
	}
}