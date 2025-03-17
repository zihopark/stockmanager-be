# 네이버 스마트스토어 기반 원재료 재고 관리 프로그램


Project Structure 
```plaintext
📦main
 ┣ 📂java
 ┃ ┣ 📂com
 ┃ ┃ ┣ 📂stock
 ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📜CommerceApiController.java
 ┃ ┃ ┃ ┃ ┣ 📜MasterController.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialController.java
 ┃ ┃ ┃ ┃ ┣ 📜MemberController.java
 ┃ ┃ ┃ ┃ ┣ 📜TotalOrderController.java
 ┃ ┃ ┃ ┃ ┗ 📜TotalProductController.java
 ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┣ 📂naverOrder
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderDTO.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductOrderDTO.java
 ┃ ┃ ┃ ┃ ┣ 📂naverProduct
 ┃ ┃ ┃ ┃ ┃ ┣ 📜CombinedDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OptionDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductForOptionSupplementDTO.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SupplementDTO.java
 ┃ ┃ ┃ ┃ ┣ 📂object
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiDetailProductResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiOrderResponse.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ApiProductResponse.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialsFinalProductDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜MemberDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionsMaterialDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialDTO.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialsWrapper.java
 ┃ ┃ ┃ ┃ ┣ 📜StockLogDTO.java
 ┃ ┃ ┃ ┃ ┗ 📜SupplementsMaterialDTO.java
 ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┣ 📂naverOrder
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderEntity.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductOrderEntity.java
 ┃ ┃ ┃ ┃ ┣ 📂naverProduct
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OptionEntity.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductEntity.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SupplementEntity.java
 ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┣ 📂naverOrder
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OrderRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProductOrderRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂naverProduct
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OptionRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SupplementRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MaterialRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OptionsMaterialRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜SettingsRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜StockLogRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜SupplementsMaterialRepository.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜MemberEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionsMaterialEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜SettingsEntity.java
 ┃ ┃ ┃ ┃ ┣ 📜StockLogEntity.java
 ┃ ┃ ┃ ┃ ┗ 📜SupplementsMaterialEntity.java
 ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┣ 📜ApiDetailProductService.java
 ┃ ┃ ┃ ┃ ┣ 📜ApiOrderService.java
 ┃ ┃ ┃ ┃ ┣ 📜ApiProductService.java
 ┃ ┃ ┃ ┃ ┣ 📜AutoTokenService.java
 ┃ ┃ ┃ ┃ ┣ 📜ManualTokenService.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialService.java
 ┃ ┃ ┃ ┃ ┣ 📜MaterialServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┣ 📜MemberServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionService.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionsMaterialService.java
 ┃ ┃ ┃ ┃ ┣ 📜OptionsMaterialServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜OrderService.java
 ┃ ┃ ┃ ┃ ┣ 📜OrderServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductOrderService.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductOrderServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductService.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialService.java
 ┃ ┃ ┃ ┃ ┣ 📜ProductsMaterialServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜SettingsService.java
 ┃ ┃ ┃ ┃ ┣ 📜SettingsServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜StockLogService.java
 ┃ ┃ ┃ ┃ ┣ 📜StockLogServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜SupplementService.java
 ┃ ┃ ┃ ┃ ┣ 📜SupplementServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜SupplementsMaterialService.java
 ┃ ┃ ┃ ┃ ┣ 📜SupplementsMaterialServiceImpl.java
 ┃ ┃ ┃ ┃ ┣ 📜UserDetailsImpl.java
 ┃ ┃ ┃ ┃ ┗ 📜UserDetailsServiceImpl.java
 ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┣ 📜JWTUtil.java
 ┃ ┃ ┃ ┃ ┗ 📜PasswordMaker.java
 ┃ ┃ ┃ ┣ 📜.DS_Store
 ┃ ┃ ┃ ┣ 📜EnvLoader.java
 ┃ ┃ ┃ ┣ 📜JwtAuthFilter.java
 ┃ ┃ ┃ ┣ 📜StockManagementApplication.java
 ┃ ┃ ┃ ┗ 📜WebSecurityConfig.java
 ┃ ┃ ┗ 📜.DS_Store
 ┃ ┗ 📜.DS_Store
 ┗ 📂resources
   ┗ 📜application.properties

```
