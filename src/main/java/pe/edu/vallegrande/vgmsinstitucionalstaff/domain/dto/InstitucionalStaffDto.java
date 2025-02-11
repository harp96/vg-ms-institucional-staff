    package pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto;

    import java.time.LocalDate;

    import lombok.Data;

    @Data
    public class InstitucionalStaffDto {

        private String id;
        private String fatherLastname;
        private String motherLastname;
        private String name;
        private LocalDate birthdate;
        private String documentType;
        private String documentNumber;
        private String sex;
        private String country;
        private Ubigeo ubigeo;
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
