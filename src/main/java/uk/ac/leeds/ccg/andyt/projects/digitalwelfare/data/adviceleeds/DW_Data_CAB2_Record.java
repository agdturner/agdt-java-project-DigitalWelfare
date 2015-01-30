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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_StaticIO;

/**
 *
 * @author geoagdt
 */
public class DW_Data_CAB2_Record extends DW_Data_Postcode_Record {

    //enquiry_ref,client_ref,bureau,outlet,suboutlet,date_opened,enquiry_type,work_level,enquiry_project_funder,next_step,directed_to,Postcode,appt_location,Local_Authority_Name,Local_Authority_Ward_Name,Primary_Care_Trust_Name,Parliamentary_Const_Name,dob,transferred_in,client_seen_after_transfer,transferred_out,total_contacts,total_contacts_current_bureau,total_non_client_contacts_current_bureau
    private String enquiry_ref;
    private String client_ref;
    private String bureau;
    private String outlet;
    private String suboutlet;
    private String date_opened;
    private String enquiry_type;
    private String work_level;
    private String enquiry_project_funder;
    private String next_step;
    private String directed_to;
//    private String Postcode;
    private String appt_location;
    private String Local_Authority_Name;
    private String Local_Authority_Ward_Name;
    private String Primary_Care_Trust_Name;
    private String Parliamentary_Const_Name;
    private String dob;
    private String transferred_in;
    private String client_seen_after_transfer;
    private String transferred_out;
    private String total_contacts;
    private String total_contacts_current_bureau;
    private String total_non_client_contacts_current_bureau;

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public DW_Data_CAB2_Record(
            long RecordID) {
        setRecordID(RecordID);
    }

    /**
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_CAB2_Record(
            long RecordID,
            String line,
            DW_Data_CAB2_Handler handler) throws Exception {
        setRecordID(RecordID);
        
        //if (RecordID ==2925) {
        //if (RecordID ==2969) {
        //if (RecordID ==2978) {
//        if (RecordID ==3018) {
//            int Debug =1;
//        }
        
        String[] fields = DW_StaticIO.splitWithQuotesThenCommas(line);
        if (fields.length != 24) {
            System.out.println("fields.length " + fields.length);
            System.out.println("RecordID " + RecordID);
            System.out.println(line);
        }
        int n = 0;
        enquiry_ref = fields[n];
        n++;
        client_ref = fields[n];
        n++;
        bureau = fields[n];
        n++;
        outlet = fields[n];
        n++;
        suboutlet = fields[n];
        n++;
        date_opened = fields[n];
        n++;
        enquiry_type = fields[n];
        n++;
        work_level = fields[n];
        n++;
        enquiry_project_funder = fields[n];
        n++;
        next_step = fields[n];
        n++;
        directed_to = fields[n];
        n++;
        setPostcode(fields[n]);
        n++;
        appt_location = fields[n];
        n++;
        Local_Authority_Name = fields[n];
        n++;
        Local_Authority_Ward_Name = fields[n];
        n++;
        Primary_Care_Trust_Name = fields[n];
        n++;
        Parliamentary_Const_Name = fields[n];
        n++;
        dob = fields[n];
        n++;
        transferred_in = fields[n];
        n++;
        client_seen_after_transfer = fields[n];
        n++;
        transferred_out = fields[n];
        n++;
        total_contacts = fields[n];
        n++;
        total_contacts_current_bureau = fields[n];
        n++;
        total_non_client_contacts_current_bureau = fields[n];
    }

    @Override
    public String toString() {
        return super.toString()
                + ",enquiry_ref " + enquiry_ref
                + ",client_ref " + client_ref
                + ",bureau " + bureau
                + ",outlet " + outlet
                + ",suboutlet " + suboutlet
                + ",date_opened " + date_opened
                + ",enquiry_type " + enquiry_type
                + ",work_level " + work_level
                + ",enquiry_project_funder " + enquiry_project_funder
                + ",next_step " + next_step
                + ",directed_to " + directed_to
//                + ",Postcode " + Postcode
                + ",appt_location " + appt_location
                + ",Local_Authority_Name " + Local_Authority_Name
                + ",Local_Authority_Ward_Name " + Local_Authority_Ward_Name
                + ",Primary_Care_Trust_Name " + Primary_Care_Trust_Name
                + ",Parliamentary_Const_Name " + Parliamentary_Const_Name
                + ",dob " + dob
                + ",transferred_in " + transferred_in
                + ",client_seen_after_transfer " + client_seen_after_transfer
                + ",transferred_out " + transferred_out
                + ",total_contacts " + total_contacts
                + ",total_contacts_current_bureau " + total_contacts_current_bureau
                + ",total_non_client_contacts_current_bureau " + total_non_client_contacts_current_bureau;
    }

    public String getEnquiry_ref() {
        return enquiry_ref;
    }

    public void setEnquiry_ref(String enquiry_ref) {
        this.enquiry_ref = enquiry_ref;
    }

    public String getClient_ref() {
        return client_ref;
    }

    public void setClient_ref(String client_ref) {
        this.client_ref = client_ref;
    }

    public String getBureau() {
        return bureau;
    }

    public void setBureau(String bureau) {
        this.bureau = bureau;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getSuboutlet() {
        return suboutlet;
    }

    public void setSuboutlet(String suboutlet) {
        this.suboutlet = suboutlet;
    }

    public String getDate_opened() {
        return date_opened;
    }

    public void setDate_opened(String date_opened) {
        this.date_opened = date_opened;
    }

    public String getEnquiry_type() {
        return enquiry_type;
    }

    public void setEnquiry_type(String enquiry_type) {
        this.enquiry_type = enquiry_type;
    }

    public String getWork_level() {
        return work_level;
    }

    public void setWork_level(String work_level) {
        this.work_level = work_level;
    }

    public String getEnquiry_project_funder() {
        return enquiry_project_funder;
    }

    public void setEnquiry_project_funder(String enquiry_project_funder) {
        this.enquiry_project_funder = enquiry_project_funder;
    }

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    public String getDirected_to() {
        return directed_to;
    }

    public void setDirected_to(String directed_to) {
        this.directed_to = directed_to;
    }

    public String getAppt_location() {
        return appt_location;
    }

    public void setAppt_location(String appt_location) {
        this.appt_location = appt_location;
    }

    public String getLocal_Authority_Name() {
        return Local_Authority_Name;
    }

    public void setLocal_Authority_Name(String Local_Authority_Name) {
        this.Local_Authority_Name = Local_Authority_Name;
    }

    public String getLocal_Authority_Ward_Name() {
        return Local_Authority_Ward_Name;
    }

    public void setLocal_Authority_Ward_Name(String Local_Authority_Ward_Name) {
        this.Local_Authority_Ward_Name = Local_Authority_Ward_Name;
    }

    public String getPrimary_Care_Trust_Name() {
        return Primary_Care_Trust_Name;
    }

    public void setPrimary_Care_Trust_Name(String Primary_Care_Trust_Name) {
        this.Primary_Care_Trust_Name = Primary_Care_Trust_Name;
    }

    public String getParliamentary_Const_Name() {
        return Parliamentary_Const_Name;
    }

    public void setParliamentary_Const_Name(String Parliamentary_Const_Name) {
        this.Parliamentary_Const_Name = Parliamentary_Const_Name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTransferred_in() {
        return transferred_in;
    }

    public void setTransferred_in(String transferred_in) {
        this.transferred_in = transferred_in;
    }

    public String getClient_seen_after_transfer() {
        return client_seen_after_transfer;
    }

    public void setClient_seen_after_transfer(String client_seen_after_transfer) {
        this.client_seen_after_transfer = client_seen_after_transfer;
    }

    public String getTransferred_out() {
        return transferred_out;
    }

    public void setTransferred_out(String transferred_out) {
        this.transferred_out = transferred_out;
    }

    public String getTotal_contacts() {
        return total_contacts;
    }

    public void setTotal_contacts(String total_contacts) {
        this.total_contacts = total_contacts;
    }

    public String getTotal_contacts_current_bureau() {
        return total_contacts_current_bureau;
    }

    public void setTotal_contacts_current_bureau(String total_contacts_current_bureau) {
        this.total_contacts_current_bureau = total_contacts_current_bureau;
    }

    public String getTotal_non_client_contacts_current_bureau() {
        return total_non_client_contacts_current_bureau;
    }

    public void setTotal_non_client_contacts_current_bureau(String total_non_client_contacts_current_bureau) {
        this.total_non_client_contacts_current_bureau = total_non_client_contacts_current_bureau;
    }

}
