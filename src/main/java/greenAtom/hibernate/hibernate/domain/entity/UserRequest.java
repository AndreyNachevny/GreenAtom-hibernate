package greenAtom.hibernate.hibernate.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private static int ELS_PER_PAGE = 5;

    private int currentPage;

    private int ageFilter;

    private String nameFilter;

    private Long cashFilter;

    private String isWoman;

    public Map<String, Object> getCriteria() {
        Map<String, Object> cr = new HashMap<>();
        if (nameFilter != null) {
            cr.put("name", nameFilter);
        }
        if (cashFilter != null) {
            cr.put("cash", cashFilter);
        }
        if (ageFilter != 0) {
            cr.put("age", ageFilter);
        }
        if (isWoman != null) {
            cr.put("isWoman", Boolean.valueOf(isWoman));
        }
        return cr;
    }
}
