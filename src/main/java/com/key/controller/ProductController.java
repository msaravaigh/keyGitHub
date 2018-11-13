package com.key.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.key.model.Category;
import com.key.model.Color;
import com.key.model.Customer;
import com.key.model.GroupCode;
import com.key.model.InvoiceDetail;
import com.key.model.Location;
import com.key.model.Manufacture;
import com.key.model.Model;
import com.key.model.ProductMaster;
import com.key.model.ProductMasterModel;
import com.key.model.SubCategory;
import com.key.model.Unit;
import com.key.model.User;
import com.key.model.Vendor;
import com.key.service.CategoryService;
import com.key.service.ColorService;
import com.key.service.CompanyService;
import com.key.service.CustomerService;
import com.key.service.GroupCodeService;
import com.key.service.LocationService;
import com.key.service.ManufactureService;
import com.key.service.ModelService;
import com.key.service.ProductMasterService;
import com.key.service.SubCategoryService;
import com.key.service.UnitService;
import com.key.service.UserService;
import com.key.service.VendorService;
import com.key.util.ApplicationConstants;
import com.key.util.CommonUtils;


@RestController
@CrossOrigin
public class ProductController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ColorService colorService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private GroupCodeService groupCodeService;
	
	
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ManufactureService manufactureService;
	
	@Autowired
	private ModelService modelService;
	@Autowired
	private SubCategoryService subCategoryService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private VendorService vendorService;
	
	@Autowired
	private ProductMasterService productMasterService;
	
	
	@RequestMapping(value={"/getpp"}, method = RequestMethod.GET)
	public ResponseEntity<?> getpp(){
		
		
		ResponseEntity<InvoiceDetail> entity=null;
		
		InvoiceDetail pp = new InvoiceDetail();
				entity=new ResponseEntity<>(pp, HttpStatus.OK);
			
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BYID_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProduct(@PathVariable("id") int id,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BYID_URL) ){
					ProductMaster product =productMasterService.getProductMasterById(id);;
					System.out.println("product ==== "+product);
					response.put("output", product);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BYICODE_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByItemsCode(@RequestParam(name ="itemCode" ) String itemCode,
						@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BYICODE_URL) ){
					List<ProductMaster> products=null;
					if(itemCode!=null && !"".equals(itemCode.trim())){
						products =productMasterService.findByItemCode(itemCode);
						System.out.println("product ==== "+products);
						response.put("output", products);
						response.put("msg", "Success");
						entity= new ResponseEntity<>(response, HttpStatus.OK);
					}
					else {
						response.put("msg", "Input missing");
						entity=new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
					}
					
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BYI_NAME_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByItemsName(@RequestParam(name ="any" , required = false) Boolean any,
			@RequestParam(name ="itemName" ) String itemName,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BYI_NAME_URL) ){
					List<ProductMaster> products=null;
					if(itemName!=null && !"".equals(itemName.trim())){
						if(any!=null && any ==true){
							System.out.println("..called for any ");
							products =productMasterService.matchByItemName(itemName);
						}else{
							products =productMasterService.findByItemName(itemName);	
						}
						
						System.out.println("product ==== "+products);
						response.put("output", products);
						response.put("msg", "Success");
						entity= new ResponseEntity<>(response, HttpStatus.OK);
					}else {
						response.put("msg", "Input missing");
						entity=new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
					}
					
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BY_GR_CODE_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByGroupCode(@RequestParam(name ="groupCode" , required = false) Integer groupCode,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BY_GR_CODE_URL) ){
					List<ProductMaster> products=null;
						products =productMasterService.findByGroupCode(groupCode);
					
					System.out.println("product ==== "+products);
					response.put("output", products);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BY_CAT_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByCategory(@RequestParam(name ="categoryId" , required = false) Integer categoryId,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BY_CAT_URL) ){
					List<ProductMaster> products=null;
						products =productMasterService.findByCategory(categoryId);
					
					System.out.println("product ==== "+products);
					response.put("output", products);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BY_MANF_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByManufacture(@RequestParam(name ="manufactureId" , required = false) Integer manufactureId,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BY_MANF_URL) ){
					List<ProductMaster> products=null;
						products =productMasterService.findByManufacture(manufactureId);
					
					System.out.println("product ==== "+products);
					response.put("output", products);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_PRODUCT_BY_VENDOR_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getProductByVendor(@RequestParam(name ="vendorId" , required = false) Integer vendorId,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_PRODUCT_BY_VENDOR_URL) ){
					List<ProductMaster> products=null;
						products =productMasterService.findByVendor(vendorId);
					
					System.out.println("product ==== "+products);
					response.put("output", products);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_POST_PRODUCT_SAVE_URL}, method = RequestMethod.POST)
	public ResponseEntity<?> saveProduct(@RequestBody ProductMasterModel productMasterModel,
			@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization, UriComponentsBuilder ucBuilder){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_POST_PRODUCT_SAVE_URL) ){
					
					ProductMaster product = setProductMasterEntity(productMasterModel);
					
					product =productMasterService.saveProductMaster(product);
					System.out.println("product ==== "+product);
					response.put("output", product);
					response.put("msg", "Success");
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}

	public ProductMaster setProductMasterEntity(ProductMasterModel productMasterModel) {
		ProductMaster product = new ProductMaster();
		
		product.setActPurCost(productMasterModel.getActPurCost());
		
		product.setCategory(categoryService.findById(productMasterModel.getCategoryId()));
		product.setColor(colorService.findById(productMasterModel.getColorId()));
		product.setCompany(companyService.findById(productMasterModel.getCompanyId()));
		product.setDangerLevel(productMasterModel.getDangerLevel());
		product.setEanCodeType(productMasterModel.getEanCodeType());
		product.setGroupCode(groupCodeService.findById(productMasterModel.getGroupCodeId()));
//					product.setInvoiceDetails(productMasterModel.getInvoiceDetails());
//					product.setIsActive(isActive);
		product.setIsAssetItem(productMasterModel.getIsAssetItem());
		product.setIsCancelled(productMasterModel.getIsCancelled());
//					product.setIsDeleted(isDeleted);
		product.setIsFocAllowed(productMasterModel.getIsFocAllowed());
		product.setIsIncludePromotion(productMasterModel.getIsIncludePromotion());
		product.setIsWorkItem(productMasterModel.getIsWorkItem());
		product.setItemAvgCost(productMasterModel.getItemAvgCost());
		product.setItemBarCode(productMasterModel.getItemBarCode());
		product.setItemCategorySno(productMasterModel.getItemCategorySno());
		product.setItemCode(productMasterModel.getItemCode());
		product.setItemDiscount(productMasterModel.getItemDiscount());
		product.setItemLocationName(productMasterModel.getItemLocationName());
		product.setItemMinSellPrice(productMasterModel.getItemMinSellPrice());
		product.setItemName(productMasterModel.getItemName());
		product.setItemSellPrice(productMasterModel.getItemSellPrice());
		product.setItemShopCost(productMasterModel.getItemShopCost());
		product.setItemTenderPrice(productMasterModel.getItemTenderPrice());
		product.setItemWSalePrice(productMasterModel.getItemWSalePrice());
		product.setLocation(locationService.findById(productMasterModel.getLocationId()));
		product.setMadeinId(productMasterModel.getMadeinId());
		product.setManufacture(manufactureService.findById(productMasterModel.getManufactureId()));
		product.setMaxLevel(productMasterModel.getMaxLevel());
		product.setMinLevel(productMasterModel.getMinLevel());
		product.setModel(modelService.findById(productMasterModel.getModelId()));
		
		product.setPriceCode(productMasterModel.getPriceCode());
		product.setPurchaseUnit(unitService.findById(productMasterModel.getPurchaseUnitId()));
		product.setReoRderLevel(productMasterModel.getReoRderLevel());
		product.setSalesUnit(unitService.findById(productMasterModel.getSalesUnitId()));
		product.setSubCategory(subCategoryService.findById(productMasterModel.getSubCategoryId()));
		product.setUnit(unitService.findById(productMasterModel.getUnitId()));
		product.setVendor(vendorService.findById(productMasterModel.getVendorId()));
		return product;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_CATEGORY_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllCategory(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_CATEGORY_URL) ){
					List<Category> categories =categoryService.getAllCategory();
					System.out.println("categories ==== "+categories);
					response.put("output", categories);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_COLOR_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllColor(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_COLOR_URL) ){
					List<Color> colors =colorService.getAllColor();
					System.out.println("colors ==== "+colors);
					response.put("output", colors);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_CUSTOMER_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllCustomer(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_CUSTOMER_URL) ){
					List<Customer> customers =customerService.getAllCustomer();
					System.out.println("customers ==== "+customers);
					response.put("output", customers);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_GROUP_CODE_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllGroupCode(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_GROUP_CODE_URL) ){
					List<GroupCode> groupCodes =groupCodeService.getAllGroupCode();
					System.out.println("groupCodes ==== "+groupCodes);
					response.put("output", groupCodes);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_LOCATION_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllLocation(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_LOCATION_URL) ){
					List<Location> locations =locationService.getAllLocation();
					System.out.println("locations ==== "+locations);
					response.put("output", locations);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_MANUFACTURE_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllManufacture(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_MANUFACTURE_URL) ){
					List<Manufacture> manufactures =manufactureService.getAllManufacture();
					System.out.println("manufactures ==== "+manufactures);
					response.put("output", manufactures);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
	@RequestMapping(value={ApplicationConstants.REQ_GET_MODEL_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllModel(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_MODEL_URL) ){
					List<Model> models =modelService.getAllModel();
					System.out.println("models ==== "+models);
					response.put("output", models);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	@RequestMapping(value={ApplicationConstants.REQ_GET_SUBCATEGORY_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllSubCategory(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_SUBCATEGORY_URL) ){
					List<SubCategory> subCategories =subCategoryService.getAllSubCategory();
					System.out.println("subCategories ==== "+subCategories);
					response.put("output", subCategories);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	@RequestMapping(value={ApplicationConstants.REQ_GET_UNIT_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllUnit(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_UNIT_URL) ){
					List<Unit> units =unitService.getAllUnit();
					System.out.println("units ==== "+units);
					response.put("output", units);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	@RequestMapping(value={ApplicationConstants.REQ_GET_VENDOR_URL}, method = RequestMethod.GET)
	public ResponseEntity<?> getAllVendor(@RequestHeader(value="Authorization", defaultValue="Unauthorised") String authorization){
		
		
		Map<String, Object> response = new ManagedMap<>();
		ResponseEntity<Map<String, Object>> entity=null;
		
			User accountUser = userService.findUserByEmail(userService.extractUsername(authorization));
			if(accountUser!=null){
				if(CommonUtils.validateUserPermission(accountUser,ApplicationConstants.REQ_GET_VENDOR_URL) ){
					List<Vendor> Vendors =vendorService.getAllVendor();
					System.out.println("Vendors ==== "+Vendors);
					response.put("output", Vendors);
					entity= new ResponseEntity<>(response, HttpStatus.OK);
				}else{
					response.put("msg", "User does not have permission..");
					entity=new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
				}
			}else{
				response.put("msg", "User not found.");
				entity=new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		
		return entity;
	}
	
}
