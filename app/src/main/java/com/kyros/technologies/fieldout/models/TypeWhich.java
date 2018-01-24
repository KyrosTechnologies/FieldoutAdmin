package com.kyros.technologies.fieldout.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by kyros on 23-01-2018.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class TypeWhich{
    private int id;
    private String type;
    private String customFieldId;
    public TypeWhich(){

    }


}
