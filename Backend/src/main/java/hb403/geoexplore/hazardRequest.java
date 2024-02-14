package hb403.geoexplore;

public class hazardRequest {
    private Address pendAddress;
    private String pendHazard;
    private boolean pendBlock;
    private String[] bannedWords = {"Will be filled with curse words and maybe some words that could add confusion instead of clarity"};

    /*
     * Costructor to temporarily make a request to run a couple checks to approve
     * the hazard to go into the database.
     * 
     * @param address
     * 
     * @param hazard
     * 
     * @param isBlockingRoad
     */
    public hazardRequest(Address address, String hazard, boolean isBlockingRoad) {
        this.pendAddress = address;
        this.pendHazard = hazard;
        this.pendBlock = isBlockingRoad;
    }

    /*
     * Returns the type of hazard when called
     * 
     * @return pendHazard
     */
    public String getHazard() {
        return pendHazard;
    }

    /*
     * Since this would be a publicly viewed application it probably would have a
     * filter to make sure people aren't cussing each other out or anoything like
     * that.
     * There would also probably be a list of blacklisted words to avoid confusion
     * and there would probably have to be some sort of human reviewer for some of
     * the hazards that are listed as "Other"
     * @return boolean
     */
    public boolean checkHazard() {
        boolean passed = true;
        for (int i = 0; i < bannedWords.length; i++){
            if (pendHazard.contains(bannedWords[i])){
                passed = false;
            }
        }
        return passed;
    }

    /*
     * Checks if the hazard is blocking the road could maybe alter the icon or make another one on the road itself(an x or something)
     */
    public boolean getBlocking() {
        return pendBlock;
    }

    public boolean check(hazardRequest pending) {
        if (pending.pendAddress.getCity() == "Ames" && checkHazard() == true) {
            return true;// another layer of detail would be checking in the database of the actual names
                        // of streets in ames to make sure they match
        } else {
            return false;
        }
    }

}
