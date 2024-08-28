<details> 
  <summary>Hint 1: How to stream data to a browser? </summary>
   - Use either Server-Sent Events (SSE) or WebSockets. The former is easier.
    <details> 
        <summary>Hint 1.1: How to use SSE in an MVC app? </summary>
        - Use `org.springframework.web.servlet.mvc.method.annotation.SseEmitter` in your controller as the return type of requests handler methods.
    </details>
    <details> 
        <summary>Hint 1.2: How to use SSE in a Reactive app? </summary>
        - Use `reactor.core.publisher.Flux` in your controller as the return type of requests handler methods. 
          Also, set `org.springframework.web.bind.annotation.GetMapping.produces` to `MediaType.TEXT_EVENT_STREAM_VALUE`
    </details>
</details>
<details> 
  <summary>Hint 2: How to read stream data from an endpoint? </summary>
   - Use one of:
     - org.springframework.web.client.RestTemplate (e.g. execute method)
     - org.springframework.web.client.RestClient (e.g. exchange method)
     - org.springframework.web.reactive.function.client.WebClient
     - java.net.http.HttpClient (e.g. send method + `HttpResponse.BodyHandlers.ofLines()`)
</details>
