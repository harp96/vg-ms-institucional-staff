package pe.edu.vallegrande.vgmsinstitucionalstaff.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "institucional_staff")
public class InstitucionalStaff {

    @Id
    private String id;
    private String fatherLastname;
    private String motherLastname;
    private String name;
    private LocalDate birthdate;
    private String documentType;
    private String documentNumber;
    private String sex;
    private String country;
    private String ubigeo;
    private String email;
    private String phone;
    private String civilStatus;
    private String instructionGrade;
    private String disabilityType;
    private String disability;
    private String workCondition;
    private String occupation;
    private String nativeLanguage;
    private String status;
    private String address;
    private String rol;

}
