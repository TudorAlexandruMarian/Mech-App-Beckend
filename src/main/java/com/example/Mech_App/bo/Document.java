package com.example.Mech_App.bo;

import com.example.Mech_App.enums.DocumentType;
import com.example.Mech_App.utils.Audit;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long serviceEntryId;
    String docPath;
    @Enumerated(EnumType.STRING)
    DocumentType documentType;
}
