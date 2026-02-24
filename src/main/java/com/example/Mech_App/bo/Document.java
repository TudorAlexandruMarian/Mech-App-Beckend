package com.example.Mech_App.bo;

import com.example.Mech_App.enums.DocumentType;
import com.example.Mech_App.utils.Audit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Document extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long serviceEntryId;
    String docPath;
    @Enumerated(EnumType.STRING)
    DocumentType documentType;
}
