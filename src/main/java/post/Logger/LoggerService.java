package post.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import post.Service.CommentService;

public class LoggerService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public static void main(String[] args) {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
    }
}

