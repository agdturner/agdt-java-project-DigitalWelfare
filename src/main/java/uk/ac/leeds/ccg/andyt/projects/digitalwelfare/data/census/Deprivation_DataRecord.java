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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census;

/**
 *
 * @author geoagdt
 */
public class Deprivation_DataRecord {

    /**
     * 0 RecordID
     */
    protected long RecordID;

    private String LSOACode;
    private String LACode;
    private String LAName;
    private String GORCode;
    private String GORName;
    private String IMDScore;
    private String RankOfIMDScoreForEngland;

    /**
     * Creates a null record in case this is needed
     *
     * @param RecordID
     */
    public Deprivation_DataRecord(
            long RecordID) {
        this.RecordID = RecordID;
    }

    /**
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public Deprivation_DataRecord(
            long RecordID,
            String line,
            Deprivation_DataHandler handler) throws Exception {
        this.RecordID = RecordID;
        String[] fields = line.split(",");
        int n = 0;
        LSOACode = fields[n];
        n++;
        LACode = fields[n];
        n++;
        LAName = fields[n];
        n++;
        GORCode = fields[n];
        n++;
        GORName = fields[n];
        n++;
        IMDScore = fields[n];
        n++;
        RankOfIMDScoreForEngland = fields[n];
    }

    @Override
    public String toString() {
        return "RecordID " + RecordID
                + ",LSOACode " + LSOACode
                + ",LACode " + LACode
                + ",LAName " + LAName
                + ",GORCode " + GORCode
                + ",GORName " + GORName
                + ",IMDScore " + IMDScore
                + ",RankOfIMDScoreForEngland " + RankOfIMDScoreForEngland;
    }

    public long getRecordID() {
        return RecordID;
    }

    public void setRecordID(long RecordID) {
        this.RecordID = RecordID;
    }

    public String getLSOACode() {
        return LSOACode;
    }

    public void setLSOACode(String LSOACode) {
        this.LSOACode = LSOACode;
    }

    public String getLACode() {
        return LACode;
    }

    public void setLACode(String LACode) {
        this.LACode = LACode;
    }

    public String getLAName() {
        return LAName;
    }

    public void setLAName(String LAName) {
        this.LAName = LAName;
    }

    public String getGORCode() {
        return GORCode;
    }

    public void setGORCode(String GORCode) {
        this.GORCode = GORCode;
    }

    public String getGORName() {
        return GORName;
    }

    public void setGORName(String GORName) {
        this.GORName = GORName;
    }

    public String getIMDScore() {
        return IMDScore;
    }

    public void setIMDScore(String IMDScore) {
        this.IMDScore = IMDScore;
    }

    public String getRankOfIMDScoreForEngland() {
        return RankOfIMDScoreForEngland;
    }

    public void setRankOfIMDScoreForEngland(String RankOfIMDScoreForEngland) {
        this.RankOfIMDScoreForEngland = RankOfIMDScoreForEngland;
    }

}
