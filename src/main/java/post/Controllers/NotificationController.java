package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import post.APIResponse.APIResponse;

import post.Repositories.NotificationRepo;
import post.Service.NotificationService;
@RestController
public class NotificationController {
    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notifications")
    public ResponseEntity<APIResponse> getAllNotifications(@RequestParam (required = false, defaultValue = "false") boolean all){
       return  notificationService.getAllNotifications(all);
    }


}
