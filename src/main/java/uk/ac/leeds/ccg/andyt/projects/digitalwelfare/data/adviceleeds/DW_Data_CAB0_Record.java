/*
 * Copyright (C) 2014 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_IO;

/**
 *
 * @author geoagdt
 */
public class DW_Data_CAB0_Record extends DW_Data_Postcode_Record {

    /**
     * 0 1 RecordType
     */
    protected String RecordType;
    /**
     * 1 2 HousingBenefitClaimReferenceNumber
     */
    //Financial_Year_Display,Quarter_In_Fin_Year,Client_Ref,Age_Range,Age,Gender,Disability,Health_Problems,Local_Code,Occupation,Ethnicity,Nationality,Preferred_Languages,Read_English,Speak_English,Write_English,Income_Profile,Household_Type,Housing_Tenure,Marital_Status,Child_Dependents,Adult_Dependents,Postcode,Local_Authority_Name,Local_Authority_Ward,Constituency,Westminster_MP,Primary_Care_Trust_Name,Organisation,Child_Dep_Under14,Child_Dep_Over14,Clients_Local_Bureau,Spare_Field_1,Spare_Field_2,no_in_household,Last_Updated_By,Last_Updated,DOB

    //Financial_Year_Display,Quarter_In_Fin_Year,Client_Ref,Age_Range,Age,Gender,Disability,Health_Problems,Local_Code,Occupation,Ethnicity,Nationality,Preferred_Languages,Read_English,Speak_English,Write_English,Income_Profile,Household_Type,Housing_Tenure,Marital_Status,Child_Dependents,Adult_Dependents,Postcode,Local_Authority_Name,Local_Authority_Ward,Constituency,Westminster_MP,Primary_Care_Trust_Name,Organisation,Child_Dep_Under14,Child_Dep_Over14,Clients_Local_Bureau,Spare_Field_1,Spare_Field_2,no_in_household,DOB
    //Client Profile ID,,,,,,,Client GUID,,,,,,,,,,,,,Age Group,Age,,Gender,Disability,,,Type Of Disability,Local Code1,Local Code2,Local Code3,Local Code4,Local Code5,Local Code6,Occupation,Ethnicity,Nationality,First Language,Second Language,Preferred Method Of Contact,Income Profile,Household Type,Housing Tenure,Marital Status,Child Dependants Under14,Child Dependants Over14, Postal Code,Local Authority,Local Authority Ward,Parliamentary Constituency, Westminster MP, Primary Care Trust,Date Of Birth,Religion,Sexual Orientation
    protected String Financial_Year_Display;
    protected String Quarter_In_Fin_Year;
    protected String Client_Ref;
    protected String Age_Range;
    protected String Age;
    protected String Gender;
    protected String Disability;
    protected String Health_Problems;
    protected String Local_Code;
    protected String Occupation;
    protected String Ethnicity;
    protected String Nationality;
    protected String Preferred_Languages;
    protected String Read_English;
    protected String Speak_English;
    protected String Write_English;
    protected String Income_Profile;
    protected String Household_Type;
    protected String Housing_Tenure;
    protected String Marital_Status;
    protected String Child_Dependents;
    protected String Adult_Dependents;
//    protected String Postcode;
    protected String Local_Authority_Name;
    protected String Local_Authority_Ward;
    protected String Constituency;
    protected String Westminster_MP;
    protected String Primary_Care_Trust_Name;
    protected String Organisation;
    protected String Child_Dep_Under14;
    protected String Child_Dep_Over14;
    protected String Clients_Local_Bureau;
    protected String Spare_Field_1;
    protected String Spare_Field_2;
    protected String no_in_household;
    protected String DOB;

    public DW_Data_CAB0_Record(DW_Environment env) {
        super(env);
    }

    /**
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_CAB0_Record(
            DW_Environment env,
            long RecordID,
            String line,
            DW_Data_CAB0_Handler handler) throws Exception {
        super(env);
        setRecordID(RecordID);
//        if (RecordID == 233) {
//            int debug = 1;
//        }
        String[] fields;
        fields = DW_IO.splitWithQuotesThenCommas(line);
        int fieldCount = fields.length;
//        if (fieldCount != 36) {
        if (fieldCount != 38) {
            System.out.println("RecordID " + RecordID + ", fieldCount" + fieldCount);
        }
        int n = initMostFields(fields, fieldCount);
//        n++;
//        if (n < fieldCount) {
//            DOB = fields[n];
//        }
        DOB = fields[fieldCount - 1];
    }

    protected final int initMostFields(
            String[] fields,
            int fieldCount) {
        int n = 0;
        Financial_Year_Display = fields[n];
        n++;
        if (n < fieldCount) {
            Quarter_In_Fin_Year = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Client_Ref = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Age_Range = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Age = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Gender = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Disability = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Health_Problems = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Local_Code = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Occupation = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Ethnicity = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Nationality = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Preferred_Languages = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Read_English = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Speak_English = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Write_English = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Income_Profile = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Household_Type = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Housing_Tenure = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Marital_Status = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Child_Dependents = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Adult_Dependents = fields[n];
        }
        n++;
        if (n < fieldCount) {
            setPostcode(fields[n]);
        }
        n++;
        if (n < fieldCount) {
            Local_Authority_Name = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Local_Authority_Ward = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Constituency = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Westminster_MP = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Primary_Care_Trust_Name = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Organisation = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Child_Dep_Under14 = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Child_Dep_Over14 = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Clients_Local_Bureau = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Spare_Field_1 = fields[n];
        }
        n++;
        if (n < fieldCount) {
            Spare_Field_2 = fields[n];
        }
        n++;
        if (n < fieldCount) {
            no_in_household = fields[n];
        }
        return n;
    }

    @Override
    public String toString() {
        return toStringPart1() + ",DOB " + DOB;
    }

    protected String toStringPart1() {
        return super.toString()
                + ",Financial_Year_Display " + Financial_Year_Display
                + ",Quarter_In_Fin_Year " + Quarter_In_Fin_Year
                + ",Client_Ref " + Client_Ref
                + ",Age_Range " + Age_Range
                + ",Age " + Age
                + ",Gender " + Gender
                + ",Disability " + Disability
                + ",Health_Problems " + Health_Problems
                + ",Local_Code " + Local_Code
                + ",Occupation " + Occupation
                + ",Ethnicity " + Ethnicity
                + ",Nationality " + Nationality
                + ",Preferred_Languages " + Preferred_Languages
                + ",Read_English " + Read_English
                + ",Speak_English " + Speak_English
                + ",Write_English " + Write_English
                + ",Income_Profile " + Income_Profile
                + ",Household_Type " + Household_Type
                + ",Housing_Tenure " + Housing_Tenure
                + ",Marital_Status " + Marital_Status
                + ",Child_Dependents " + Child_Dependents
                + ",Adult_Dependents " + Adult_Dependents
//                + ",Postcode " + Postcode
                + ",Local_Authority_Name " + Local_Authority_Name
                + ",Local_Authority_Ward " + Local_Authority_Ward
                + ",Constituency " + Constituency
                + ",Westminster_MP " + Westminster_MP
                + ",Primary_Care_Trust_Name " + Primary_Care_Trust_Name
                + ",Organisation " + Organisation
                + ",Child_Dep_Under14 " + Child_Dep_Under14
                + ",Child_Dep_Over14 " + Child_Dep_Over14
                + ",Clients_Local_Bureau " + Clients_Local_Bureau
                + ",Spare_Field_1 " + Spare_Field_1
                + ",Spare_Field_2 " + Spare_Field_2
                + ",no_in_household " + no_in_household;
    }

    public String getRecordType() {
        return RecordType;
    }

    public void setRecordType(String RecordType) {
        this.RecordType = RecordType;
    }

    public String getFinancial_Year_Display() {
        return Financial_Year_Display;
    }

    public void setFinancial_Year_Display(String Financial_Year_Display) {
        this.Financial_Year_Display = Financial_Year_Display;
    }

    public String getQuarter_In_Fin_Year() {
        return Quarter_In_Fin_Year;
    }

    public void setQuarter_In_Fin_Year(String Quarter_In_Fin_Year) {
        this.Quarter_In_Fin_Year = Quarter_In_Fin_Year;
    }

    public String getClient_Ref() {
        return Client_Ref;
    }

    public void setClient_Ref(String Client_Ref) {
        this.Client_Ref = Client_Ref;
    }

    public String getAge_Range() {
        return Age_Range;
    }

    public void setAge_Range(String Age_Range) {
        this.Age_Range = Age_Range;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getDisability() {
        return Disability;
    }

    public void setDisability(String Disability) {
        this.Disability = Disability;
    }

    public String getHealth_Problems() {
        return Health_Problems;
    }

    public void setHealth_Problems(String Health_Problems) {
        this.Health_Problems = Health_Problems;
    }

    public String getLocal_Code() {
        return Local_Code;
    }

    public void setLocal_Code(String Local_Code) {
        this.Local_Code = Local_Code;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public String getEthnicity() {
        return Ethnicity;
    }

    public void setEthnicity(String Ethnicity) {
        this.Ethnicity = Ethnicity;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public String getPreferred_Languages() {
        return Preferred_Languages;
    }

    public void setPreferred_Languages(String Preferred_Languages) {
        this.Preferred_Languages = Preferred_Languages;
    }

    public String getRead_English() {
        return Read_English;
    }

    public void setRead_English(String Read_English) {
        this.Read_English = Read_English;
    }

    public String getSpeak_English() {
        return Speak_English;
    }

    public void setSpeak_English(String Speak_English) {
        this.Speak_English = Speak_English;
    }

    public String getWrite_English() {
        return Write_English;
    }

    public void setWrite_English(String Write_English) {
        this.Write_English = Write_English;
    }

    public String getIncome_Profile() {
        return Income_Profile;
    }

    public void setIncome_Profile(String Income_Profile) {
        this.Income_Profile = Income_Profile;
    }

    public String getHousehold_Type() {
        return Household_Type;
    }

    public void setHousehold_Type(String Household_Type) {
        this.Household_Type = Household_Type;
    }

    public String getHousing_Tenure() {
        return Housing_Tenure;
    }

    public void setHousing_Tenure(String Housing_Tenure) {
        this.Housing_Tenure = Housing_Tenure;
    }

    public String getMarital_Status() {
        return Marital_Status;
    }

    public void setMarital_Status(String Marital_Status) {
        this.Marital_Status = Marital_Status;
    }

    public String getChild_Dependents() {
        return Child_Dependents;
    }

    public void setChild_Dependents(String Child_Dependents) {
        this.Child_Dependents = Child_Dependents;
    }

    public String getAdult_Dependents() {
        return Adult_Dependents;
    }

    public void setAdult_Dependents(String Adult_Dependents) {
        this.Adult_Dependents = Adult_Dependents;
    }

    public String getLocal_Authority_Name() {
        return Local_Authority_Name;
    }

    public void setLocal_Authority_Name(String Local_Authority_Name) {
        this.Local_Authority_Name = Local_Authority_Name;
    }

    public String getLocal_Authority_Ward() {
        return Local_Authority_Ward;
    }

    public void setLocal_Authority_Ward(String Local_Authority_Ward) {
        this.Local_Authority_Ward = Local_Authority_Ward;
    }

    public String getConstituency() {
        return Constituency;
    }

    public void setConstituency(String Constituency) {
        this.Constituency = Constituency;
    }

    public String getWestminster_MP() {
        return Westminster_MP;
    }

    public void setWestminster_MP(String Westminster_MP) {
        this.Westminster_MP = Westminster_MP;
    }

    public String getPrimary_Care_Trust_Name() {
        return Primary_Care_Trust_Name;
    }

    public void setPrimary_Care_Trust_Name(String Primary_Care_Trust_Name) {
        this.Primary_Care_Trust_Name = Primary_Care_Trust_Name;
    }

    public String getOrganisation() {
        return Organisation;
    }

    public void setOrganisation(String Organisation) {
        this.Organisation = Organisation;
    }

    public String getChild_Dep_Under14() {
        return Child_Dep_Under14;
    }

    public void setChild_Dep_Under14(String Child_Dep_Under14) {
        this.Child_Dep_Under14 = Child_Dep_Under14;
    }

    public String getChild_Dep_Over14() {
        return Child_Dep_Over14;
    }

    public void setChild_Dep_Over14(String Child_Dep_Over14) {
        this.Child_Dep_Over14 = Child_Dep_Over14;
    }

    public String getClients_Local_Bureau() {
        return Clients_Local_Bureau;
    }

    public void setClients_Local_Bureau(String Clients_Local_Bureau) {
        this.Clients_Local_Bureau = Clients_Local_Bureau;
    }

    public String getSpare_Field_1() {
        return Spare_Field_1;
    }

    public void setSpare_Field_1(String Spare_Field_1) {
        this.Spare_Field_1 = Spare_Field_1;
    }

    public String getSpare_Field_2() {
        return Spare_Field_2;
    }

    public void setSpare_Field_2(String Spare_Field_2) {
        this.Spare_Field_2 = Spare_Field_2;
    }

    public String getNo_in_household() {
        return no_in_household;
    }

    public void setNo_in_household(String no_in_household) {
        this.no_in_household = no_in_household;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

}
