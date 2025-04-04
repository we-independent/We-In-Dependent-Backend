## 🚀 Feature: Blog Article PDF Export

This brach adds the capability to display blog article list based on request.

---

## 📌 Scope of Changes

### 🎯 Added


### 🧱 Modified
- `BlogArticleController.java`: Change to `POST` 
- `BlogArticleServiceImpl.java`: Added error handling for pagination
                                 Change to `BeanUtils.copyProperties(blogArticleQry,blogArticleDO);`

## 🧪 Swagger Testing

### ✅ Environment
- Local MySQL with database
- Spring Boot app running on
- Swagger-UI: http://localhost:8080/swagger-ui/index.html
- `/dashboard/blog/list`

### ✅ Result
- Endpoint `/dashboard/blog/list` returns valid json file
- Tested edge cases (empty pageNum)

### 🛠️ Local Config Changes
- Updated `application.yaml`:
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/
    username: root
    password: yourpsw
Commented out @SignatureAuth for local access:
// @SignatureAuth
Full swagger-ui address: http://localhost:8080/swagger-ui/index.html


🚧 To-Do Before Merge
 Re-enable @SignatureAuth in Controller
 Remove or reset application.yaml DB credentials
 Review naming conventions and method annotations


## 🚧 Development Progress

- [x] Controller endpoint defined
- [x] Service method implemented
- [x] Swagger-UI testing completed

---

## 🔗 More Details

## 📬 Contact

开发者：@Hurely  
日期：2025-04-3  