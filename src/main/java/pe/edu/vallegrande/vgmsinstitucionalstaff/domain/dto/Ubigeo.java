package pe.edu.vallegrande.vgmsinstitucionalstaff.domain.dto;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Ubigeo {

    @Id
    private String idUbigeo;        
    private String ubigeoReniec;    
    private String department;       
    private String province;         
    private String district;         
    private String region;           
    private String status; 

}