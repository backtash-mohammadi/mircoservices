# Working with RestTemplate in Spring Boot

Spring's `RestTemplate` is a powerful, synchronous client that allows you to interact with HTTP servers. Below, we'll go through several common operations with `RestTemplate`.

## Prerequisites
Make sure to have the following in your Spring Boot project:

- Spring Web dependency in your `pom.xml` or `build.gradle` file.
- A RestTemplate bean configured in your configuration class.
    - Configuring `RestTemplate` as a bean ensures that it can be easily injected and managed by the Spring container (Remember `@Autowired` and injection over a constructor)

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## How to send a GET Request and retrieve the response?

To make a GET request follow these steps:

### Step 1: Create a DTO Class

Firstly, define a Data Transfer Object (DTO) that will hold the response data from your GET request.

```java
public class YourResponseDto {
    // Define fields here that map to the JSON response structure
    // Provide getters and setters
}
```

### Step 2: Make the GET Request and Store the Response in DTO

Inject Bean instance of the `RestTemplate` (e.g. into a service) and use it to make the GET request, storing the result in your DTO.

```java
@Service
public class YourService {

    @Autowired
    private RestTemplate restTemplate;

    public YourResponseDto getForObjectExample(String url) {
        // Make the GET request and get the response as YourDto
        YourResponseDto response = restTemplate.getForObject(url, YourResponseDto.class);
        return response;
    }
}
```

## How to send a POST Request with a request body?

To send a POST request you will need two DTOs: One for the request (called _payload_) and one for the expected response (called _response body_).

### Step 1: Create DTO Classes for the Request and Response

First, define a DTO class that represents the data structure you want to send as the request body.

```java
public class YourRequestDto {
    // Define fields that make up the request body
    // Provide getters and setters
}
```

Next, define another DTO class for the response you expect to receive.

```java
public class YourResponseDto {
    // Define fields that make up the response body
    // Provide getters and setters
}
```

### Step 2: Create a Method to Send the POST Request and Receive the Response

As for a GET request, you best encapsulate the HTTP logic in a service. Inject the `RestTemplate` into the service and expose a method where you send the POST request. `postForObject` is used to send the POST request with `YourRequestDto` as the body. The `url` parameter is the URL to which the POST request is sent, `requestDto` is the actual request body, and `YourResponseDto.class` specifies the type of the object in which the response should be returned.

```java
@Service
public class YourService {

    @Autowired
    private RestTemplate restTemplate;

    public YourResponseDto postForObjectExample(String url, YourRequestDto requestDto) {
        // Send the POST request with YourRequestDto as the body and get the response as YourResponseDto
        YourResponseDto response = restTemplate.postForObject(url, requestDto, YourResponseDto.class);
      return response;
    }
}
```

## How to send a PUT/DELETE Request with `RestTemplate`?

Unlike GET and POST methods, `RestTemplate` does not have a `putForObject` method. This is because PUT HTTP method is usually used for updating resources and does not typically return a body. However, you can still send a PUT request using the `exchange` method if you need to process a response body. If you do not need the response body you can also use the `put` method directly. The same is true for DELETE requests. Use either `exchange` or `delete`

### Step 1: Create a DTO Class for the PUT Request Body and a DTO class for the Response Body

Just like with POST requests, define a DTO class for the data you want to send in your PUT request body.

```java
public class YourUpdateDto {
    // Define fields for the update request
    // Provide getters and setters
}
```

If you want to capture the response of the PUT request create a second DTO.
```java
public class UpdateResponseDTO {
  // Define fields for the update reponse
  // Provide getters and setters
}
```

### Step 2: Use `exchange` or `put` to Send the PUT Request

To send a PUT request, you can use the `exchange` method (more code, but more control) or the `put` method (simple code, but less configuration possible).

#### Using `exchange`:

```java
@Service
public class YourService {

    @Autowired
    private RestTemplate restTemplate;

    public UpdateResponseDTO updateResource(String url, YourUpdateDto updateDto) {
        HttpEntity<YourUpdateDto> requestEntity = new HttpEntity<>(updateDto);
        ResponseEntity<UpdateResponseDTO> responseDTO = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, UpdateResponseDTO.class);
        
        return responseDTO.getBody();
    }
}
```

#### Using `put`:
Here you can not capture e JSON response. The same applies for the `restTemplate.delete` method.

```java
public void updateResourceSimple(String url, YourUpdateDto updateDto) {
    restTemplate.put(url, updateDto);
}
```

# Further useful tips
## How to Handle error response codes from the upstream API?

An exception is thrown for 4xx and 5xx status codes, indicating a client or server error, which needs to be handled appropriately.
```java
    try {
        YourDto result = restTemplate.getForObject(url, YourDto.class);
    } catch (HttpClientErrorException ex) {
        // Handle 4xx errors -> your fault ;)
    } catch (HttpServerErrorException ex) {
        // Handle 5xx errors -> fault of upstream api
        System.out.println("Server error occurred: " + ex.getStatusCode());
    }
```

## How to send a Header in the request?
Headers are important as they carry metadata for the HTTP request, such as authentication tokens, cookies, and information about the body content.

To send headers with your request, use an `HttpEntity` to wrap any request headers.

### Step 1: Create HttpHeaders and Set Your Header

```java
HttpHeaders headers = new HttpHeaders();
headers.set("Header-Name", "Header-Value");
```

### Step 2: Make the Request with Headers

```java
public YourDto getWithHeaders(String url) {
    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

    ResponseEntity<YourDto> response = restTemplate.exchange(
        url, HttpMethod.GET, entity, YourDto.class);

    return response.getBody();
}
```

Replace `"Header-Name"` and `"Header-Value"` with your actual header name and value.