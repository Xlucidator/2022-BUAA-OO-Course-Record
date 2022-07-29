public class Tiaozheng {
    public String simple(String ans) {
        String ans1;

        if (ans.equals("")) {
            ans1 = "0";
            return ans1;
        }
        if (ans.indexOf("+") == 0) {
            ans1 = ans.substring(1);
            return ans1;
        } else {
            if (ans.contains("+")) {
                int c = ans.indexOf("+");
                ans1 = ans.substring(c) + ans.substring(0, c - 1);
                return ans1;
            }
        }

        return ans;
    }

}
