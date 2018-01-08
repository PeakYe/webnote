public class TryClass {
    public static void main(String[] args) {
        try {
            System.out.println("before");
            throw new RuntimeException("");
            //System.out.println("after");
//        } catch(Exception e){
//            System.out.println("exp");
        } finally {
            System.out.println("args = [" + args + "]");
        }

    }
}
