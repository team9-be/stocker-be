# 📈 STOCK'ER

### 대용량 데이터와 트랜잭션을 빠르고 안정적으로 처리, 피크 시간의 트래픽도 순조롭게 관리—당신의 주식 매매, 우리가 믿음직하게 지원합니다!👍



## 📚Team Jira 보러가기

[Jira 보러가기](https://dojunkim.atlassian.net/wiki/spaces/hanghaefin/overview?homepageId=131587)



## ✨Team Brochure 보러가기

// TODO : CHANGE LINK
[Brochure 보러가기](https://misty-composer-643.notion.site/NETicket-2b06067c2b2448faa71951f43f225a0a)
<br>



# 💁🏻‍♂️프로젝트 소개

// TODO: FIX CONTENT?

```jsx
🔍 안정성과 성능
대규모 트래픽 부하 테스트를 nGrinder로 수행하여, 시스템의 안정성을 최적화했습니다. 한 번에 50K의 주식 매매 요청을 1000 TPS 처리량과 평균 응답 속도 약 1초 내외로 빠르고 안정적으로 처리합니다.

🔒 보안
Spring Security를 통해 사용자 인증과 권한 관리를 철저히 처리, 안전한 거래 환경을 제공합니다.

📊 모니터링과 분석
Grafana와 Prometheus를 활용하여 실시간 모니터링과 성능 분석을 실행, 시스템의 안정성과 성능을 지속적으로 유지합니다.

🚀 데이터 캐싱과 실시간 이벤트 처리
Redis와 Redis Pub/Sub를 활용해 빠른 데이터 조회와 캐싱을 제공하며, 실시간 이벤트 처리와 알림을 가능하게 합니다.

🗂️ DB 인덱싱 & DB Locking
데이터 조회의 효율성을 높이기 위한 DB 인덱싱을 적용하고, DB Locking 메커니즘으로 동시성 문제와 데이터 무결성을 확보, 거래와 정보 조회에서의 지연 시간과 오류 확률을 최소화합니다.

STOCK&apos;ER 은 고성능, 철저한 보안, 데이터의 효율성, 그리고 트랜잭션의 안정성을 동시에 제공합니다. 
"No Error, Just Secure, Efficient, and Ultra-Stable Trading!"
```



# 👨‍👨‍👧‍👦 Team

| 이름    | GITHUB                          |
| ------- | ------------------------------- |
| 김도준🔰 | https://github.com/downside154  |
| 이의진  | https://github.com/powerlife145 |
| 최현성  | https://github.com/ByHorizon    |
| 이선우  | https://github.com/tjsdn9803    |

---



# 💻주요기능

<details>
<summary>주요기능🧐</summary>
<div markdown="1">

### 📌 Redis Cache를 사용한 빠른 랭킹 조회

// TODO: CREATE GIF FILE OR IMAGE 

- 

### 📌 Redis의 Pub/Sub을 이용한 매매 시스템 구현

// TODO: CREATE GIF FILE OR IMAGE 


- 

### 📌 DB 인덱싱으로 매매한 종목을 내 계좌 저장 및 조회

// TODO: CREATE GIF FILE OR IMAGE 


- 

### 📌 Spring Security를 이용한 유저 시스템

// TODO: CREATE GIF FILE OR IMAGE, DELETE ENTIRELY?


- 

</div>
</details>

---

# 🎀프로젝트 챌린지 포인트

// TODO : FIX CONTENT

<details>
<summary>📈 사용자의 원활하고 끊김 없는 서비스 제공 위한 높은 TPS와 빠른 응답속도</summary>
<div markdown="1">


​    

    저희 프로젝트의 주요 목표는 사용자가 항상 원활하고 끊김 없는 서비스를 이용할 수 있도록 하는 것입니다. 이를 위해, 대규모 트랜잭션 상황에서도 안정적이면서도 높은 TPS와 빠른 응답속도를 제공하기 위해 다양한 기술적 요소를 적절하게 활용하였습니다.
    
    우선, 분산 처리 아키텍처와 In-memory caching, Database Tuning 등의 기술을 조합하여 안정적이면서도 높은 TPS와 빠른 응답속도를 실현하였습니다. 
    특히, Redis Cache를 활용하여 In-memory Data Store를 구축하여 응답속도를 크게 향상했습니다.
    
    또한, 부하 분산을 위한 Load Balancing과 자원 확장 및 축소를 자동으로 처리하는 Auto Scaling을 도입하여, 서버 부하를 적절하게 분산하고 트래픽 변화에 따라 적절한 자원을 할당함으로써, 트래픽 급증 시에도 끊김 없는 서비스를 제공할 수 있도록 구성하였습니다. 
    
    마지막으로, HikariCP와 MySQL을 Tuning 하여 Database 연결을 최적화하고, 서버 분산을 통해 병목 현상을 예방하여 안정적인 서비스를 구현하였습니다. 
    
    이러한 다양한 기술적 요소들을 적절하게 조합하여, 저희 서비스는 높은 성능과 안정성을 동시에 유지할 수 있게 되었습니다. 이를 통해 사용자들은 언제나 원활하고 끊김 없는 서비스를 경험할 수 있게 되었습니다.

</div>
</details>    

// TODO : FIX CONTENT
<details>
<summary>🕸 데이터의 정확성과 일관성을 보장해, 데이터 무결성 확보</summary>
<div markdown="1">


​    

    저희는 Redis를 도입하여 높은 TPS와 빠른 응답 속도를 확보하였으나, 중복 데이터로 인한 데이터의 일관성과 정확성 문제가 생겼습니다. 이에 대응하여 데이터 무결성을 확보하기 위해 아래와 같은 캐시 전략을 수립하였습니다.
    
    먼저 쓰기 전략으로 Write Back 방식을 도입하여, 티켓의 남은 좌석 수 데이터 수정 시 캐시에만 변경사항이 기록되고, 주기적으로 또는 특정 조건이 충족될 때 Database에 동기화합니다. 이를 통해 빠른 응답 시간과 Database의 부하를 줄일 수 있습니다. 
    
    특히, Redis의 Single Thread 특성과 원자적 연산을 사용해 락을 사용하지 않고도 동시성 제어를 하여 데이터 무결성을 확보할 수 있었습니다.
    
    읽기 전략은 Look Aside 방식을 도입하여 클라이언트가 특정 데이터를 읽을 때마다 캐시를 먼저 확인하고, Cache miss의 경우 Database에서 Data를 가져와 캐시에 저장한 후 클라이언트에 반환합니다. 이 방식을 통해 Database와 캐시 간의 일관성을 유지할 수 있습니다.
    
    종합적으로 Redis를 통해 대규모 트랜잭션 상황에서의 동시성 제어를 하면서, 위의 캐시 전략으로 데이터의 정확성과 일관성을 보장해 데이터 무결성을 확보할 수 있었습니다.

</div>
</details>    


// TODO : FIX CONTENT
<details>
<summary>📊 APM을 활용해서 시스템의 성능과 안정성을 지속적으로 관리</summary>
<div markdown="1">


​    

    대규모 트랜잭션 상황에서 동시성 제어를 수행하며, 프로젝트의 챌린지 포인트 중 하나로 APM을 활용한 모니터링을 도입하였습니다. 
    이를 통해 시스템의 성능과 안정성을 지속적으로 관리하고 개선할 수 있었습니다. 
    
    저희 팀은 Grafana, Cloud Watch 및 Pinpoint와 같은 다양한 모니터링 도구를 사용하여 시스템의 전반적인 성능을 실시간으로 확인하였습니다.
     
    특히, Grafana와 Cloud Watch를 통해 EC2, Elastic Cache, ALB, RDS, Auto Scaling 등의 상태를 실시간으로 모니터링할 수 있었습니다. 
    
    또한, Pinpoint를 사용하여 병목 현상이 나타나는 지점을 확인하고 개선할 수 있었습니다. 이를 통해 서비스의 안정성과 성능을 지속적으로 유지하고 개선할 수 있었으며, 사용자들에게 최상의 서비스 경험을 제공할 수 있게 되었습니다. 
    
    종합적으로, 이러한 모니터링 도구들의 활용을 통해 시스템 내 문제가 발생했을 때 빠르게 진단하고 수정 및 개선 작업을 수행할 수 있었습니다. 결과적으로 프로젝트는 안정성과 높은 성능을 보장하는 성공적인 구현이 이루어졌습니다.  

</div>
</details>    

<details>
<summary>📉 비용을 최소화하면서도 높은 가용성과 성능을 유지하는 가성비 최적화 전략</summary>
<div markdown="1">


​    

    저희는 최소 비용으로도 높은 가용성과 성능을 유지하는 것을 목표로 하며, 이를 위해 다양한 기술적인 방법과 전략적인 설계를 채택하였습니다. 
    
    EC2는 직전 버전보다 20% 저렴한 Graviton2 arm64 아키텍쳐 기반에, 무료로도 사용 가능한 t4g.small 서버를 사용했습니다. 서버 확장이 필요할 경우 비용이 더 발생하는 Scale Up 방식보다 Load Balancing을 이용해 Scale Out 방식의 수평적 확장으로 비용을 최소화 하였습니다.
    
    공연 예매 사이트의 특성상 예매 오픈 시간대에 트래픽이 집중되는 현상이 발생합니다. 그래서 Auto Scaling을 설정해 오픈 시간 직전과 트랜잭션이 몰리는 상황에서만 서버 인스턴스 확장을 하고, 서버 부하가 없는 대부분의 시간에는 서버 인스턴스가 최소로 유지됩니다. 서버 수를 동적으로 조절하여 자원 사용량을 최적화하고 비용 절감 효과를 극대화할 수 있었습니다.
    
    이를 통해 높은 가용성과 성능을 유지하면서도 비용을 최소화하는 것뿐만 아니라, 가성비 측면에서도 최적의 결과를 얻을 수 있도록 했습니다.

</div>
</details>    

---

# 📚 기술 스택

## ⚙ Architecture 구성도

![img](blob:https://dojunkim.atlassian.net/1557e2ee-e215-49a3-b5b8-1448343d7e3a#media-blob-url=true&id=00fa213e-5f6b-42b0-96ae-beeab52014a0&collection=contentId-4685849&contextId=4685849&mimeType=image%2Fpng&name=%E1%84%8C%E1%85%AE%E1%86%BC%E1%84%80%E1%85%A1%E1%86%AB%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD%E1%84%8B%E1%85%A1%E1%84%8F%E1%85%B5%E1%84%90%E1%85%A6%E1%86%A8%E1%84%8E%E1%85%A7.drawio%20(1).png&size=756772&height=613&width=1271&alt=)


## ⚔ 프로젝트에서 사용한 기술

### *💻 Backend*

---

📚 **Tech Stack**


<img src="https://img.shields.io/badge/JAVA 17-6DB33F?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring JPA-6DB33F?style=flat&logo=&logoColor=white"/>  
<img src="https://img.shields.io/badge/JWT-6DB33F?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis Cache-DC382D?style=flat&logo=redis&logoColor=white"/>
<!-- <img src="https://img.shields.io/badge/QueryDSL-7957D5?style=flat&logo=&logoColor=white"/> -->

🔩 **DB**

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>  



🗜 **Cloud**

<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/>
<img src="https://img.shields.io/badge/AWS S3-FF9900?style=flat&logo=amazons3&logoColor=white"/>  
<img src="https://img.shields.io/badge/AWS Elastic Load Balancer-6DB33F?style=flat&logo=&logoColor=white"/>  

💻 **Deployment**
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white"/> 
<img src="https://img.shields.io/badge/AWS Code Delploy-6DB33F?style=flat&logo=&logoColor=white"/>  
<img src="https://img.shields.io/badge/GitHub Actions-F05032?style=flat&logo=&logoColor=white"/> 


⚖ **Test**

<img src="https://img.shields.io/badge/Junit5-25A162?style=flat&logo=junit5&logoColor=white"/>  <img src="https://img.shields.io/badge/Mockito-6DB33F?style=flat&logo=&logoColor=white"/>  <img src="https://img.shields.io/badge/Jmeter-D22128?style=flat&logo=apachejmeter&logoColor=white"/>  
<img src="https://img.shields.io/badge/Ngrinder-7957D5?style=flat&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white"/>  



🖥 **Monitoring**

<img src="https://img.shields.io/badge/AWS CloudWatch-FF4F8B?style=flat&logo=amazoncloudwatch&logoColor=white"/>  <img src="https://img.shields.io/badge/Grafana-F46800?style=flat&logo=grafana&logoColor=white"/>  <img src="img.shields.io/badge/Prometheus-E6522C?style=flat&logo=Prometheus&logoColor=white">



---

## 🏹 기술적 의사 결정

[기술적 의사 결정 과정 보러가기](https://dojunkim.atlassian.net/wiki/spaces/hanghaefin/pages/4685849/MVP)

## 🗺 API 명세서

[API 명세서 보러가기](https://dojunkim.atlassian.net/wiki/spaces/hanghaefin/pages/360573/API)

## 💾 ERD

// TODO: ADD IMAGE SRC

![img](blob:https://dojunkim.atlassian.net/b1630fb7-9487-436a-b7c8-edc95c3ee165#media-blob-url=true&id=e40f92c0-7b27-4950-abfc-e9a7bbca6b7d&collection=contentId-33270&contextId=33270&height=600&width=1227&alt=)



---



# 👾 Trouble Shooting

### 🌟 매매 서비스 로직에서 데이터 무결성을 지키며 응답속도와 TPS 개선 🌟


// TODO: FIX CONTENT

<aside>
💡 챌린지 포인트로 설정한 목표를 달성하기 위해<br>
예매 서비스 로직에서 ‘남은 좌석 수’의 데이터 무결성을 지켜야 하고,<br>
많은 트랜잭션을 처리하여 클라이언트에게 빠른 응답 속도로 예매 결과를 돌려줘야 합니다.
</aside>

<br><br>

// TODO : FIX CONTENT AND LINKS

[1. 트랜잭션 충돌 문제를 비관적 락으로 해결](https://misty-composer-643.notion.site/1-2ace0d67e5be4082a3fc3153de662e73)<br>
[2. 비관적 락 적용 시 속도 문제를 Redis Cache로 해결](https://misty-composer-643.notion.site/2-Redis-Cache-58069ce89a91466d94fe7d1d7ca0812b)<br>
[3. 더 높은 목표치를 Scale Out으로 해결](https://misty-composer-643.notion.site/3-Scale-Out-036894647e974fde914d91195eb83420)<br>
[4. 서버 확장으로 인한 비용 문제를 AutoScaling으로 해결](https://misty-composer-643.notion.site/4-AutoScaling-e8c4f28c5ea545a284400422ec865794)<br>
[5. APM으로 찾은 병목현상을 Hikari, MySQL 튜닝으로 해결](https://misty-composer-643.notion.site/5-APM-Hikari-MySQL-fd64de87205e4c59a4bdf31ed0b5627f)<br>



### 🌟 조회 서비스 로직에서 Redis cache 응답속도와 TPS 개선  🌟

// TODO: CREATE GIF FILE OR IMAGE 




---

# 🚀 성능 개선

<aside>
// TODO : CHANGE CONTENT

🛠 저희는 트러블 슈팅의 각 단계에서 성능이 어느 정도까지 개선되었는지 알아보기 위해,<br>
프로젝트 내내 Apache Jmeter를 통해 부하테스트를 진행했습니다.<br>
평균 응답 속도(ms)와 처리량(TPS, Transacion Per Second)을 위주로 정리해 두었습니다.<br>


</aside>
<br>

[성능개선 항목 보러가기](https://misty-composer-643.notion.site/17f7b104d38e40aaa75f1bd8d6dc9619)

---

