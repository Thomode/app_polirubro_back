package app.polirubro.pagination.utils;

import app.polirubro.pagination.dto.PaginationInfo;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtility {
    @Resource
    private Environment env;
    public <T extends JpaRepository> PaginationInfo getPaginationInfo(String property, T repository) {
        int pageSize = Integer.parseInt(env.getProperty(property));
        int totalRooms = (int) repository.count();
        int totalPages = totalRooms / pageSize;
        if (totalRooms % pageSize != 0) {
            totalPages++;
        }

        return PaginationInfo.builder()
                .totalNumberOfPages(totalPages)
                .totalNumberOfElements(totalRooms)
                .build();
    }
}
