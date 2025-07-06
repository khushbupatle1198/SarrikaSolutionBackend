package com.sds.Dao;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
@Transactional
public class SDSDaoImpl implements SDSDao {

    @PersistenceContext
    private EntityManager entityManager;
    
    private static final Logger logger = LoggerFactory.getLogger(SDSDaoImpl.class);
    
    // Course Methods
    public void saveCourse(Course course) {
        entityManager.persist(course);
    }

    public List<Course> getAllCourses() {
        return entityManager.createQuery("FROM Course", Course.class).getResultList();
    }

    public Course getCourseById(Long id) {
        return entityManager.find(Course.class, id);
    }

    public void updateCourse(Course course) {
        entityManager.merge(course);
    }

    public void deleteCourse(Long id) {
        Course c = getCourseById(id);
        if (c != null) {
            entityManager.remove(c);
        }
    }

 // EReport Methods
    @Override
    public EReport saveEReport(EReport eReport) {
        entityManager.persist(eReport);
        return eReport;
    }

    @Override
    public List<EReport> getAllEReports() {
        TypedQuery<EReport> query = entityManager.createQuery(
            "FROM EReport", EReport.class);
        return query.getResultList();
    }

    @Override
    public EReport getEReportById(Long id) {
        return entityManager.find(EReport.class, id);
    }

    @Override
    public EReport updateEReport(EReport eReport) {
        // First merge the eReport to get managed entity
        EReport managedEReport = entityManager.merge(eReport);
        
        // Explicitly handle the points collection
        if (eReport.getPoints() != null) {
            managedEReport.setPoints(new ArrayList<>(eReport.getPoints()));
        }
        
        return managedEReport;
    }

    @Override
    public void deleteEReport(Long id) {
        EReport eReport = getEReportById(id);
        if (eReport != null) {
            entityManager.remove(eReport);
        }
    }
    
    
    //CUNSOLTATION
    @Override
    public List<Consultation> getAllConsultations() {
        return entityManager.createQuery("FROM Consultation", Consultation.class).getResultList();
    }

    @Override
    public Consultation getConsultationById(Long id) {
        return entityManager.find(Consultation.class, id);
    }

    @Override
    public Consultation saveConsultation(Consultation consultation) {
        entityManager.persist(consultation);
        return consultation;
    }

    @Override
    public Consultation updateConsultation(Consultation consultation) {
        return entityManager.merge(consultation);
    }

    @Override 
    public void deleteConsultation(Long id) {
        Consultation consultation = getConsultationById(id);
        if (consultation != null) {
            entityManager.remove(consultation);
        }
    }
    
 // Banner Methods
    @Override
    public Banner saveBanner(Banner banner) {
        entityManager.persist(banner);
        return banner;
    }

    @Override
    public List<Banner> getAllBanners() {
        return entityManager.createQuery("FROM Banner", Banner.class).getResultList();
    }

    @Override
    public Banner getBannerById(Long id) {
        return entityManager.find(Banner.class, id);
    }

    @Override
    public Banner updateBanner(Banner banner) {
        return entityManager.merge(banner);
    }

    @Override
    public void deleteBanner(Long id) {
        Banner banner = getBannerById(id);
        if (banner != null) {
            entityManager.remove(banner);
        }
    }
    
 // BLOGS
    @Override
    public List<Blog> getAllBlogs() {
        return entityManager.createQuery("FROM Blog ORDER BY date DESC", Blog.class).getResultList();
    }

    @Override
    public Blog getBlogById(Long id) {
        return entityManager.find(Blog.class, id);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        entityManager.persist(blog);
        return blog;
    }

    @Override
    public Blog updateBlog(Blog blog) {
        return entityManager.merge(blog);
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = getBlogById(id);
        if (blog != null) {
            entityManager.remove(blog);
        }
    }

    @Override
    public List<Blog> getFeaturedBlogs() {
        return entityManager.createQuery("FROM Blog WHERE featured = true ORDER BY date DESC", Blog.class)
                .setMaxResults(3)
                .getResultList();
    }

    @Override
    public List<Blog> getBlogsByCategory(String category) {
        return entityManager.createQuery("FROM Blog WHERE category = :category ORDER BY date DESC", Blog.class)
                .setParameter("category", category)
                .getResultList();
    }
    
    
 // Add these methods to your SDSDaoImpl class
    @Override
    public Video saveVideo(Video video) {
        entityManager.persist(video);
        return video;
    }

    @Override
    public List<Video> getVideosByCourseId(Long courseId) {
        return entityManager.createQuery(
            "FROM Video WHERE course.id = :courseId", Video.class)
            .setParameter("courseId", courseId)
            .getResultList();
    }

    @Override
    public Video getVideoById(Long id) {
        return entityManager.find(Video.class, id);
    }

    @Override
    public void deleteVideo(Long id) {
        Video video = getVideoById(id);
        if (video != null) {
            entityManager.remove(video);
        }
    }
    
    @Override
    public Video updateVideo(Video video) {
        return entityManager.merge(video);
    }
    
 // Implement in SDSDaoImpl.java:
    @Override
    public User findUserByEmail(String email) {
        try {
            return entityManager.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User saveUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public CoursePurchase savePurchase(CoursePurchase purchase) {
        entityManager.persist(purchase);
        return purchase;
    }

    @Override
    public CoursePurchase getPurchaseById(Long id) {
        return entityManager.find(CoursePurchase.class, id);
    }

    @Override
    public CoursePurchase updatePurchase(CoursePurchase purchase) {
        return entityManager.merge(purchase);
    }
    
    @Override
    public User updateUser(User user) {
        return entityManager.merge(user);
    }
    
    @Override
    public List<CoursePurchase> getAllPurchases() {
        return entityManager.createQuery(
            "FROM CoursePurchase ORDER BY purchaseDate DESC", CoursePurchase.class)
            .getResultList();
    }

    @Override
    public List<CoursePurchase> getPurchasesByStatus(String status) {
        return entityManager.createQuery(
            "FROM CoursePurchase WHERE status = :status ORDER BY purchaseDate DESC", CoursePurchase.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<CoursePurchase> getPurchasesByUser(Long userId) {
        return entityManager.createQuery(
            "FROM CoursePurchase WHERE user.id = :userId ORDER BY purchaseDate DESC", 
            CoursePurchase.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public User findUserByResetToken(String token) {
        try {
            return entityManager.createQuery("FROM User WHERE resetToken = :token", User.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ConsultationBooking saveBooking(ConsultationBooking booking) {
        entityManager.persist(booking);
        return booking;
    }

    @Override
    public List<ConsultationBooking> getBookingsByStatus(String status) {
        return entityManager.createQuery(
            "FROM ConsultationBooking WHERE status = :status ORDER BY bookingDate DESC", 
            ConsultationBooking.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public List<ConsultationBooking> getAllBookings() {
        return entityManager.createQuery(
            "FROM ConsultationBooking ORDER BY bookingDate DESC", ConsultationBooking.class)
            .getResultList();
    }

    @Override
    public ConsultationBooking getBookingById(Long id) {
        return entityManager.find(ConsultationBooking.class, id);
    }

    @Override
    public void updateBooking(ConsultationBooking booking) {
        entityManager.merge(booking);
        entityManager.flush(); // Ensure changes are persisted immediately
    }
	
    
    @Override
    public EReportPurchase saveEReportPurchase(EReportPurchase purchase) {
        entityManager.persist(purchase);
        return purchase;
    }

    @Override
    public List<EReportPurchase> getEReportPurchasesByStatus(String status) {
        return entityManager.createQuery(
            "SELECT p FROM EReportPurchase p LEFT JOIN FETCH p.eReport WHERE p.status = :status ORDER BY p.purchaseDate DESC", 
            EReportPurchase.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public List<EReportPurchase> getAllEReportPurchases() {
        return entityManager.createQuery(
            "SELECT p FROM EReportPurchase p LEFT JOIN FETCH p.eReport ORDER BY p.purchaseDate DESC", 
            EReportPurchase.class)
            .getResultList();
    }

    @Override
    public EReportPurchase getEReportPurchaseById(Long id) {
        return entityManager.find(EReportPurchase.class, id);
    }

    @Override
    public void updateEReportPurchase(EReportPurchase purchase) {
        entityManager.merge(purchase);
        entityManager.flush();
    }

	@Override
	public Review saveReview(Review review) {
		// TODO Auto-generated method stub
		entityManager.persist(review);
		return review;
	}

	@Override
	public List<Review> getAllReviews() {
		// TODO Auto-generated method stub
		return entityManager.createQuery("from Review", Review.class).getResultList();
	}

	@Override
	public void deleteReview(Long id) {
	    Review review = entityManager.find(Review.class, id);
	    if (review != null) {
	        entityManager.remove(review);
	    }
	}
	
	@Override
	public List<User> getAllUsers() {
	    return entityManager.createQuery("FROM User ORDER BY createdAt DESC", User.class)
	            .getResultList();
	}
	
	@Override
	public long getTotalUsers() {
	    return entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class)
	            .getSingleResult();
	}

	@Override
	public long getTotalCoursePurchases() {
	    return entityManager.createQuery("SELECT COUNT(cp) FROM CoursePurchase cp", Long.class)
	            .getSingleResult();
	}

	@Override
	public long getTotalConsultationBookings() {
	    return entityManager.createQuery("SELECT COUNT(cb) FROM ConsultationBooking cb", Long.class)
	            .getSingleResult();
	}

	@Override
	public long getTotalEReportPurchases() {
	    return entityManager.createQuery("SELECT COUNT(ep) FROM EReportPurchase ep", Long.class)
	            .getSingleResult();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Double getRevenueForPeriod(String period) {
	    try {
	        Double courseRevenue = getCourseRevenueForPeriod(period);
	        Double consultationRevenue = getConsultationRevenueForPeriod(period);
	        Double eReportRevenue = getEReportRevenueForPeriod(period);
	        
	        return (courseRevenue != null ? courseRevenue : 0.0) + 
	               (consultationRevenue != null ? consultationRevenue : 0.0) + 
	               (eReportRevenue != null ? eReportRevenue : 0.0);
	    } catch (Exception e) {
	        logger.error("Error calculating revenue for period {}: {}", period, e.getMessage(), e);
	        return 0.0;
	    }
	}

	private Double getCourseRevenueForPeriod(String period) {
	    LocalDate today = LocalDate.now();
	    String query = "SELECT COALESCE(SUM(c.price), 0.0) FROM CoursePurchase cp JOIN cp.course c WHERE ";
	    query += getDateCondition(period, "cp.purchaseDate", today);
	    
	    try {
	        Query jpaQuery = entityManager.createQuery(query);
	        applyDateParameters(jpaQuery, period, today);
	        
	        Object result = jpaQuery.getSingleResult();
	        if (result instanceof Double) {
	            return (Double) result;
	        } else if (result instanceof BigDecimal) {
	            return ((BigDecimal) result).doubleValue();
	        }
	        return 0.0;
	    } catch (Exception e) {
	        logger.error("Error getting course revenue for period {}: {}", period, e.getMessage(), e);
	        return 0.0;
	    }
	}

	private Double getConsultationRevenueForPeriod(String period) {
	    LocalDate today = LocalDate.now();
	    String query = "SELECT COALESCE(SUM(con.price), 0.0) FROM ConsultationBooking cb JOIN cb.consultation con WHERE ";
	    query += getDateCondition(period, "cb.bookingDate", today);
	    
	    try {
	        Query jpaQuery = entityManager.createQuery(query);
	        applyDateParameters(jpaQuery, period, today);
	        
	        Object result = jpaQuery.getSingleResult();
	        if (result instanceof Double) {
	            return (Double) result;
	        } else if (result instanceof BigDecimal) {
	            return ((BigDecimal) result).doubleValue();
	        }
	        return 0.0;
	    } catch (Exception e) {
	        logger.error("Error getting consultation revenue for period {}: {}", period, e.getMessage(), e);
	        return 0.0;
	    }
	}

	private Double getEReportRevenueForPeriod(String period) {
	    LocalDate today = LocalDate.now();
	    String query = "SELECT COALESCE(SUM(ep.amountPaid), 0.0) FROM EReportPurchase ep WHERE ";
	    query += getDateCondition(period, "ep.purchaseDate", today);
	    
	    try {
	        Query jpaQuery = entityManager.createQuery(query);
	        applyDateParameters(jpaQuery, period, today);
	        
	        Object result = jpaQuery.getSingleResult();
	        if (result instanceof Double) {
	            return (Double) result;
	        } else if (result instanceof BigDecimal) {
	            return ((BigDecimal) result).doubleValue();
	        }
	        return 0.0;
	    } catch (Exception e) {
	        logger.error("Error getting e-report revenue for period {}: {}", period, e.getMessage(), e);
	        return 0.0;
	    }
	}

	private String getDateCondition(String period, String dateField, LocalDate today) {
	    switch (period.toLowerCase()) {
	        case "today":
	            return "DATE(" + dateField + ") = CURRENT_DATE";
	        case "yesterday":
	            return "DATE(" + dateField + ") = DATE(:yesterday)";
	        case "week":
	            return dateField + " BETWEEN :weekStart AND :weekEnd";
	        case "month":
	            return "YEAR(" + dateField + ") = :year AND MONTH(" + dateField + ") = :month";
	        case "year":
	            return "YEAR(" + dateField + ") = :year";
	        case "all":
	            return "1=1";
	        default:
	            return "1=0";
	    }
	}

	private void applyDateParameters(Query query, String period, LocalDate today) {
	    switch (period.toLowerCase()) {
	        case "yesterday":
	            query.setParameter("yesterday", today.minusDays(1));
	            break;
	        case "week":
	            LocalDate weekStart = today.with(DayOfWeek.MONDAY);
	            LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);
	            query.setParameter("weekStart", weekStart.atStartOfDay())
	                 .setParameter("weekEnd", weekEnd.atTime(LocalTime.MAX));
	            break;
	        case "month":
	            query.setParameter("year", today.getYear())
	                 .setParameter("month", today.getMonthValue());
	            break;
	        case "year":
	            query.setParameter("year", today.getYear());
	            break;
	    }
	}

	@Override
	public Map<String, Long> getCountsForPeriod(String period) {
	    LocalDate today = LocalDate.now();
	    Map<String, Long> counts = new HashMap<>();
	    
	    counts.put("coursePurchases", getCountForPeriod("CoursePurchase", "purchaseDate", period, today));
	    counts.put("consultationBookings", getCountForPeriod("ConsultationBooking", "bookingDate", period, today));
	    counts.put("eReportPurchases", getCountForPeriod("EReportPurchase", "purchaseDate", period, today));
	    
	    return counts;
	}

	private Long getCountForPeriod(String entityName, String dateField, String period, LocalDate today) {
	    String query = "SELECT COUNT(e) FROM " + entityName + " e WHERE " + 
	                  getDateCondition(period, "e." + dateField, today);
	    
	    Query jpaQuery = entityManager.createQuery(query, Long.class);
	    applyDateParameters(jpaQuery, period, today);
	    
	    return (Long) jpaQuery.getSingleResult();
	}
   
}