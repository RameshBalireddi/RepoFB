//package post.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.Errors;
//import org.springframework.validation.FieldError;
//import post.APIResponse.APIResponse;
//
//import java.util.stream.Collectors;
//
//@Component
//public class BindingResultResponse {
//    public static ResponseEntity<APIResponse> handleBindingResultErrors(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String errorMessage = bindingResult.getFieldErrors()
//                    .stream()
//                    .map(FieldError::getDefaultMessage)
//                    .collect(Collectors.joining("; "));
//            return ResponseEntity.badRequest().body(APIResponse.error(errorMessage).getBody());
//        }
//        return null;
//    }
//}
