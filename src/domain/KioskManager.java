package domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class KioskManager {
    Scanner sc = new Scanner(System.in);
    private static String[] menu = new String[]{"","면류","밥류","튀김","주류","주문","취소"};
    private Map<String,Category> categories = new LinkedHashMap<>();
    private List<Product> products = new LinkedList<>();
    private List<Order> orders = new LinkedList<>();
    private List<Order> totalOrders = new LinkedList<>();
    private Map<String, Integer> ordersCount = new HashMap<>();
    private int orderCount=1;
    private int totalAmount = 0;

    public KioskManager() {
        categories.put("면류",new Category("면  류","중식 특유의 불향이 가득한 명품 면"));
        categories.put("밥류",new Category("밥  류","중식 특유의 불향 가득한 명품 밥"));
        categories.put("튀김",new Category("튀김류","바삭한 식감과 쫄깃한 튀김"));
        categories.put("주류",new Category("주  류","중국 음식과 찰떡 주류"));

        Category c1 = categories.get("면류");
        c1.addProduct(new Product("짜장면","중식특유의 불향이 가득한 명품 짜장면",6500));
        c1.addProduct(new Product("고추짜장","달콤 담백한 짜장면에 청양고추로 아삭한 식감과 매콤함을 살린 고추짜장",7500));
        c1.addProduct(new Product("짬뽕","중식 특유의 불향 가득한 명품 짬뽕",7500));
        c1.addProduct(new Product("고추짬뽕","불 맛 가득한 짬뽕에 청양고추의 매운 맛을 더하다",8500));
        c1.addProduct(new Product("쟁반짜장(2인분)","중식특유의 불향이 가득한 명품 쟁반 짜장면",14500));

        Category c2 = categories.get("밥류");
        c2.addProduct(new Product("짬봉밥","중식 특유의 불향 가득한 명품 짬뽕밥",8000));
        c2.addProduct(new Product("고추짬뽕밥","불 맛 가득한 짬뽕에 청양고추의 매운 맛을 더하다",9000));
        c2.addProduct(new Product("짜장밥","짬뽕국물 1개 기본 제공",8500));

        Category c3 = categories.get("튀김");
        c3.addProduct(new Product("탕수육","쫄깃한 식감과 새콤달콤한 소스의 조화",14900));
        c3.addProduct(new Product("군만두","맛나는 군만두",6000));
        c3.addProduct(new Product("꽃빵","맛나는 꽃빵",3500));
        c3.addProduct(new Product("멘보샤","오동통한 새우살과 칠리소스의 환상조합",8500));
        c3.addProduct(new Product("해쉬브라운","맛나는 해쉬브라운",2500));

        Category c4 = categories.get("주류");
        c4.addProduct(new Product("국산맥주","카스,테라",4500));
        c4.addProduct(new Product("소주","참이슬,진로,새로",4500));
        c4.addProduct(new Product("칭따오","중국 전통 맥주",6500));
        c4.addProduct(new Product("만만춘","중국백주 33도",15000));
    }

    public void Init(){
        while(true){
            DrawMenu();
        }
    }
    private void DrawMenu(){
        System.out.println("\n백종원의 홍콩반점 0410에 오신것을 환영합니다.");
        System.out.println("아래 메뉴판을 보시고 메뉴를 골라 입력해주세요.");
        System.out.println();

        System.out.println("[ 홍콩반점 0410 MENU ]");
        AtomicInteger index= new AtomicInteger(1);

        categories.forEach((k,v)->{
            System.out.println(index+". "+k+"\t\t | "+v.getDescribe());
            index.getAndIncrement();
        });

        System.out.println();

        System.out.println("[ ORDER MENU ]");
        System.out.println(categories.size()+1+". Order" +"\t | "+ "장바구니를 확인 후 주문합니다.");
        System.out.println(categories.size()+2+". Cancel" +"\t | "+ "진행중인 주문을 취소합니다.");

        System.out.print("메뉴 선택 => ");

        int select = sc.nextInt();

        if(select ==0){
            secretOrders();
        }
        else if(select<=4)
            SelectMenu(select);
        else
            SelectOrder(select);
    }

    private void secretOrders(){
        printTotalSalesAmount();
        printTotalOrders();
    }
    private void SelectMenu(int select){
        String m = menu[select];

        Category c = categories.get(m);
        c.DrawProduct();

        System.out.print("메뉴 선택 => ");
        int selectMenu = sc.nextInt();

        Product p = c.getProduct(selectMenu-1);

        System.out.printf("%s | W %d | %s\n",p.getName(),p.getPrice(),p.getDescribe());
        System.out.println("위 메뉴를 장바구니에 추가하시겠습니까?");
        System.out.println("1. 확인 2. 취소");

        int isAddCart = sc.nextInt();
        if(isAddCart==1)
            AddCart(p);
        else
            return;
    }

    private void AddCart(Product product){
        orders.add(new Order(product));
        totalOrders.add(new Order(product));
        ordersCount.put(product.getName(),ordersCount.getOrDefault(product.getName(),0)+1);
    }
    private void SelectOrder(int select){
        String m = menu[select];
        int s = 0;

        switch (select){
            case 5:
                s = printOrders();
                break;

            case 6:
                cancelOrder();
                break;
        }

        if(s==1){

            orderComplete();
        }else{
            return;
        }
    }

    private int printOrders(){
        System.out.println("아래와 같이 주문 하시겠습니까?");
        System.out.println("[Orders ]");


        AtomicInteger total = new AtomicInteger();
        orders.forEach(order -> {
            Product product = order.getProduct();
            // 주문 개수 기능 추가
            // HashMap을 사용하여 구현
            // 시간 관계상 미구현
            System.out.printf("%s | W %d | %s\n",product.getName(),product.getPrice(),product.getDescribe());
            total.addAndGet(product.getPrice());
            totalAmount+=product.getPrice();
        });

        System.out.println("\n[Total ]");
        System.out.println("W "+total);

        System.out.println("1. 주문 2. 메뉴판");
        int s = sc.nextInt();

        return s;
    }

    private void orderComplete()  {
        if(orders.size()==0) {
            System.out.println("선택한 주문이 없습니다.");
            return;
        }

        orders.clear();

        System.out.println("주문이 완료되었습니다.!\n");

        System.out.printf("대기번호는 [ %d ] 번 입니다.\n",orderCount++);
        System.out.println("3초후 메뉴판으로 돌아갑니다");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cancelOrder(){
        System.out.println("진행하던 주문을 취소하시겠습니까?");
        System.out.println("1. 확인 2. 취소");

        int s = sc.nextInt();

        if(s==1){
            orders.clear();
            totalAmount = 0;
        }else{
            return;
        }
    }

    private void printTotalSalesAmount(){
        System.out.println("[ 총 판매금액 현황 ]");
        System.out.printf("현재까지 총 판매된 금액은 [W %d ] 입니다.\n",totalAmount);

        System.out.println("1. 돌아가기");
        if(sc.nextInt()==1){
            return;
        }
    }

    private void printTotalOrders(){
        System.out.println("[ 총 판매상품 목록 현황 ]");
        System.out.println("현재까지 총 판매된 상품 목록은 아래와 같습니다.");

        totalOrders.forEach(order -> {
            Product p = order.getProduct();
            System.out.printf("- %s | W %d\n",p.getName(),p.getPrice());
        });

        System.out.println("1. 돌아가기");
        if(sc.nextInt()==1){
            return;
        }
    }
}
