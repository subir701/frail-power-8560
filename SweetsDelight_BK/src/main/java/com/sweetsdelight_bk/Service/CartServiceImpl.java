package com.sweetsdelight_bk.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetsdelight_bk.Exceptions.CartsException;
import com.sweetsdelight_bk.Model.Cart;
import com.sweetsdelight_bk.Model.Product;
import com.sweetsdelight_bk.Repository.CartRepo;
import com.sweetsdelight_bk.Repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepo cartRepositoty;
	
	@Autowired
	private ProductRepo productRepository;
	@Override
	public Cart addCard(Cart cart) {
		if(cart==null)throw new CartsException("Cart shuld not be null");
		log.debug("Calling Save method from CartJpa Repository");
		Cart SavedCart=cartRepositoty.save(cart);
		log.info("Cart saved successfully");
		return SavedCart;
	}

	@Override
	public Cart updateCard(Cart cart) {
		Optional<Cart> opt=cartRepositoty.findById(cart.getCartId());
		
		if(opt.isPresent()){
			log.debug("Calling Save method from CartJpa Repository"); 		
			Cart upCart=cartRepositoty.save(cart);
			log.info("Cart updated successfully");
			return upCart;
		}
		else{
			 throw new CartsException("Cart Doen't Exist");
		}
	}

	@Override
	public String DeleteCart(Integer userId) {
		Optional<Cart> opt=cartRepositoty.findById(userId);
		if(opt.isPresent()){
			log.debug("Calling Save method from CartJpa Repository"); 	
			cartRepositoty.deleteById(userId);
			log.info("Cart deleted successfully");
			return "User Deleted";
		}
		else
		{
			throw new CartsException("user not Exist");
		}
	}

	@Override
	public List<Cart> showAllCarts() {
		log.debug("Calling findAll method from CartJpa Repository"); 
		List<Cart> allList=cartRepositoty.findAll();
		if(allList.isEmpty())throw new CartsException("No carts available");
		
		log.info("Getting all cart");
		return allList;
	}

	@Override
	public Cart addProductToCart(Integer cartId, Integer productId)throws CartsException {
		log.debug("Calling findById method from CartJpa Repository"); 
		Optional<Cart> opt=cartRepositoty.findById(cartId);
		if(opt.isEmpty())throw new CartsException("No cart found");
		Cart cart= opt.get();
		Product temp= productRepository.findById(productId).orElseThrow(()-> new CartsException("No proudct found"));
		List<Product> list= cart.getListProduct();
		list.add(temp);
		cart.setProductCount(list.size());
		List<Double> price=list.stream().map(Product :: getPrice).toList();
		Double total=0.0;
		for(Double i: price) {
			total+=i;
		}
		cart.setTotal(total);
		cart.setListProduct(list);
		return cartRepositoty.save(cart);
	}

	
	
	
	
	
}
