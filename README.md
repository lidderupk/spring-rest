# spring-rest

## Things to remember:
   - Read a resource using appcontext:
   	```
   	@Autowired
  	private ApplicationContext appContext;
  	
  	Resource resource = appContext.getResource("classpath:data.json");
  	
  	```
  	
   - Use ObjectMapper to convert File/url into json object
   
   ```
  	ObjectMapper mapper = new ObjectMapper();
	Book[] b = mapper.readValue(resource.getFile(), Book[].class);
   ```