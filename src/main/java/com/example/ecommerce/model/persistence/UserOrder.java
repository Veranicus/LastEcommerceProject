package com.example.ecommerce.model.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_order")
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	@Column
	private Long id;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonProperty
	@Column
    private List<Item> items;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false, referencedColumnName = "id")
	@JsonProperty
    private User user;
	
	@JsonProperty
	@Column
	private BigDecimal total;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public static UserOrder createFromCart(Cart cart) {
        UserOrder order = new UserOrder();
        order.setItems(cart.getItems().stream().collect(Collectors.toList()));
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOrder userOrder = (UserOrder) o;

        if (id != null ? !id.equals(userOrder.id) : userOrder.id != null) return false;
        if (items != null ? !items.equals(userOrder.items) : userOrder.items != null) return false;
        if (user != null ? !user.equals(userOrder.user) : userOrder.user != null) return false;
        return total != null ? total.equals(userOrder.total) : userOrder.total == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", items=" + items +
                ", user=" + user +
                ", total=" + total +
                '}';
    }
}
