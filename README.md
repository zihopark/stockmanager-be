# 네이버 스마트스토어 기반 원재료 재고 관리 프로그램
주문건 발송 시, 해당 제품의 원재료 재고가 자동으로 차감되는 기능을 구현
<br /><br />
## <img alt="React" src="https://img.shields.io/badge/React-61DAFB.svg?&style=for-the-badge&logo=React&logoColor=black"/> 프론트엔드 레포지토리
 
https://github.com/zihopark/stockmanager-fe
<br /><br />

## 💡 프로젝트 시작 배경

***[문제 인식]*** 

네이버 스마트스토어의 핸드메이드 판매자, 혹은 세트 상품 판매자의 경우, 원재료 재고 관리가 어려움

***[상세 문제 기술]*** 

상품 주문이 들어와 출고처리되면, 해당 상품의 재고는 빠지지만, 실제 해당 상품을 구성하는 원재료의 재고 현황은 확인이 어려움. 
**특히 하나의 재료가 여러 상품의 재료로 사용되는 경우, 더더욱 원재료 재고 파악이 어려워짐.** (하기 사진 참고)

***[해결 방안]***

**상품이 출고 처리 될 때, 자동으로 주문건에 따라 해당 상품을 구성하는 원재료 또한 출고 처리가 되도록 함.**

## 🗂️ 프로젝트 설명

***[주요 기능]***

- 매일 사용자가 설정해둔 시간에 자동으로 24시간 동안의 주문건 조회, 신규 주문건은 가져오고 발송처리된 주문건은 해당 주문 상품을 구성하는 재료 차감
- 스마트스토어에서 전체 상품 불러오기
- 전체 상품에 대한 조합형 옵션 상품, 추가상품 불러오기
- 상품, 옵션상품, 추가상품을 구성하는 재료 설정
- 재료 신규 추가, 카테고리화, 수정, 삭제 가능
- 재료 직접 매뉴얼로 입출고 기록 기능


## ⚒️ **사용 기술**

- Language: Java 17, JavaScript, HTML, CSS
- Framework : [BackEnd] Spring Boot 4 & Spring Security, [FrontEnd] React
- Database : MySQL


## 📜 **프로젝트 구조** 
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
