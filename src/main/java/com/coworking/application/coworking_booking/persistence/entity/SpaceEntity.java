package com.coworking.application.coworking_booking.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "spaces",
  uniqueConstraints = @UniqueConstraint(name = "uk_space_name", columnNames = "name"),
  indexes = {
    @Index(name = "idx_space_type", columnList = "space_type_id"),
    @Index(name = "idx_space_status", columnList = "space_status"),
    @Index(name = "idx_capacity", columnList = "capacity"),
    @Index(name = "idx_price_per_hour", columnList = "price_per_hour"),
    @Index(name = "idx_active_space", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne(optional = false)
	  @JoinColumn(name = "space_type_id")
	  private SpaceTypeEntity spaceType;

	  @Column(nullable = false, length = 100)
	  private String name;

	  @Column(columnDefinition = "TEXT")
	  private String description;

	  @Column(nullable = false)
	  private Integer capacity;

	  @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
	  private BigDecimal pricePerHour;

	  @Enumerated(EnumType.STRING)
	  @Column(name = "space_status", nullable = false, length = 20)
	  private SpaceStatus spaceStatus;

	  @Column(length = 200)
	  private String location;

	  @Column(columnDefinition = "TEXT")
	  private String equipment;

	  @Column(columnDefinition = "TEXT")
	  private String images;

	  @Column(nullable = false)
	  private boolean active = true;

	  @Column(name = "created_at", nullable = false)
	  private LocalDateTime createdAt;

	  @Column(name = "updated_at", nullable = false)
	  private LocalDateTime updatedAt;

	  public boolean getActive() {
	    return this.active;
	  }

	  public enum SpaceStatus { AVAILABLE, OCCUPIED, MAINTENANCE }	
	
}

