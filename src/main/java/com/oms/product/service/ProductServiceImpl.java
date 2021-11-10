package com.oms.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oms.product.dto.ProductDTO;
import com.oms.product.entity.Product;
import com.oms.product.exception.ProductMsException;
import com.oms.product.repository.ProductRepository;
import com.oms.product.validator.ProductValidator;
import com.oms.product.entity.SubscribedProduct;
import com.oms.product.utility.CustomPK;
import com.oms.product.repository.SubscribedProductRepository;

@Service(value = "productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SubscribedProductRepository subscribedProductRepository;
	
	private static int p;
	
	/*static {
		p=100;
	}*/
	
/*	@Override
	public String addProduct(ProductDTO productDTO) throws ProductMsException {
		
		ProductValidator.validateProduct(productDTO);
		
		Product product = productRepository.findByProductName(productDTO.getProductName());
		
		if(product != null)
			throw new ProductMsException("Service.PRODUCT_ALREADY_EXISTS");
		
		product = new Product();
		
		String id = "P"+p++;
		
		product.setProdId(id);
		product.setProductName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		product.setCategory(productDTO.getCategory());
		product.setDescription(productDTO.getDescription());
		product.setImage(productDTO.getImage());
		product.setSubCategory(productDTO.getSubCategory());
		product.setSellerId(productDTO.getSellerId());
		product.setProductRating(productDTO.getProductRating());
		product.setStock(productDTO.getStock());
		
		productRepository.save(product);
		
		return product.getProdId();
	}
*/
	public String addProduct(ProductDTO productDTO) throws ProductMsException
	{
		ProductValidator.validateProduct(productDTO);
		Product product = productRepository.findByProductName(productDTO.getProductName());
		if(product != null)
		{
			updateStockOfProd(product.getProdId(), productDTO.getStock());
			return product.getProdId()+". Just added stock as product already exists";
		}
		else
		{
			product = new Product();
//			String id = "P"+pId++;
			
			//Method1
			p=1000;
			while(true)
			{
				String pNew="P"+p;
				Product productCheck=productRepository.findByProdId(pNew);
				if(productCheck == null)
				{
					product.setProdId(pNew);
					break;
				}
				p++;
			}
			//Method2
			/*while(true)
			{
				
				int p=random.nextInt(1000);
				String pNew="P"+p;
				Product productCheck=productRepository.findByProdId(pNew);
				if(productCheck == null)
				{
					product.setProdId(pNew);
					break;
				}
			}*/
			
//			product.setProdId(id);
			product.setProductName(productDTO.getProductName());
			product.setPrice(productDTO.getPrice());
			product.setCategory(productDTO.getCategory());
			product.setDescription(productDTO.getDescription());
			product.setImage(productDTO.getImage());
			product.setSubCategory(productDTO.getSubCategory());
			product.setSellerId(productDTO.getSellerId());
			product.setProductRating(productDTO.getProductRating());
			product.setStock(productDTO.getStock());
			productRepository.save(product);
			return product.getProdId();
		}
	}
	
	@Override
	public ProductDTO getProductByName(String name) throws ProductMsException {
		
		Product product = productRepository.findByProductName(name);
		
		if(product == null)
			throw new ProductMsException("Service.PRODUCT_DOES_NOT_EXISTS");
		
		ProductDTO productDTO = new ProductDTO();
		
		productDTO.setProdId(product.getProdId());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setImage(product.getImage());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductName(product.getProductName());
		productDTO.setProductRating(product.getProductRating());
		productDTO.setSellerId(product.getSellerId());
		productDTO.setStock(product.getStock());
		productDTO.setSubCategory(product.getCategory());
		
		return productDTO;
	}
	
	@Override
	public ProductDTO getProductById(String id) throws ProductMsException {
		
		Product product = productRepository.findByProdId(id);
		
		if(product == null)
			throw new ProductMsException("Service.PRODUCT_DOES_NOT_EXISTS");
		
		ProductDTO productDTO = new ProductDTO();
		
		productDTO.setProdId(product.getProdId());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setImage(product.getImage());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductName(product.getProductName());
		productDTO.setProductRating(product.getProductRating());
		productDTO.setSellerId(product.getSellerId());
		productDTO.setStock(product.getStock());
		productDTO.setSubCategory(product.getCategory());
		
		return productDTO;
	}

	@Override
	public String deleteProduct(String id) throws ProductMsException {
		
		Product product = productRepository.findByProdId(id);
		
		if(product == null)
			throw new ProductMsException("Service.CANNOT_DELETE_PRODUCT");
		
		productRepository.delete(product);
		
		return "Product successfully deleted";
		
	}

	@Override
	public List<ProductDTO> getProductByCategory(String category) throws ProductMsException {
		
		List<Product> list = productRepository.findByCategory(category);
		
		if(list.isEmpty())
			throw new ProductMsException("Service.CATEGORY_ERROR");
		
		List<ProductDTO> li = new ArrayList<>();
		
		for(Product product : list)
		{
			ProductDTO productDTO = new ProductDTO();
			
			productDTO.setProdId(product.getProdId());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setImage(product.getImage());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductName(product.getProductName());
			productDTO.setProductRating(product.getProductRating());
			productDTO.setSellerId(product.getSellerId());
			productDTO.setStock(product.getStock());
			productDTO.setSubCategory(product.getCategory());
			
			li.add(productDTO);
		}
		
		return li;
	}

	@Override
	public Boolean updateStockOfProd(String prodId, Integer quantity) throws ProductMsException {
		Optional<Product> optProduct = productRepository.findById(prodId);
		Product product = optProduct.orElseThrow(()-> new ProductMsException("Product does not exist"));
		if(product.getStock()>=quantity) {
			product.setStock(product.getStock()-quantity);
			return true;
		}
		return false;		
	}

	@Override
	public List<ProductDTO> viewAllProducts() throws ProductMsException {
		
		List<Product> list = productRepository.findAll();
		
		if(list.isEmpty())
			throw new ProductMsException("There are no products to be shown");
		
		List<ProductDTO> li = new ArrayList<>();
		
		list.forEach(l -> {
			ProductDTO prod = new ProductDTO();
			prod.setCategory(l.getCategory());
			prod.setDescription(l.getDescription());
			prod.setImage(l.getImage());
			prod.setPrice(l.getPrice());
			prod.setProdId(l.getProdId());
			prod.setProductName(l.getProductName());
			prod.setProductRating(l.getProductRating());
			prod.setSellerId(l.getSellerId());
			prod.setStock(l.getStock());
			prod.setSubCategory(l.getSubCategory());
			
			li.add(prod);
		});
		
		return li;
	}
	
	@Override
	public String subscribeProduct(String buyerId, String prodId, Integer quantity) throws ProductMsException
	{
		Product product=productRepository.findByProdId(prodId);
		if(product == null)
		{
			throw new ProductMsException("Product does not exist");
		}
		if(product.getStock()>=quantity)
		{
			SubscribedProduct subscribedProduct=new SubscribedProduct();
			subscribedProduct.setCustomId(new CustomPK(buyerId, prodId));
			subscribedProduct.setQuantity(quantity);
			subscribedProductRepository.save(subscribedProduct);
			return "Subsribed complete for buyer with id: "+buyerId+" to product with id: "+prodId;
		}
		else
		{
			return "Not enough stock";
		}
	}


}
