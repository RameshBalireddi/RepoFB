package post.APIResponse;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class APIResponse {

    private boolean success;
    private String message;
    private Object data;

    public static ResponseEntity<APIResponse> success(String message, Object data){
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.ok(apiResponse);
    }


    public static ResponseEntity<APIResponse> error(String message){
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(message);
        apiResponse.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    public static ResponseEntity<APIResponse> uploadSuccess(String message){
        APIResponse apiResponse=new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage(message);
        apiResponse.setData(null);
        return ResponseEntity.ok(apiResponse);
    }



    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Object getData() {
        return data;
    }

}
