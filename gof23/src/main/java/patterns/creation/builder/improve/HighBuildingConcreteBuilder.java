package patterns.creation.builder.improve;

public class HighBuildingConcreteBuilder implements HouseBuilder {

    @Override
    public void buildBasic() {
        house.setBaise("地基100米");
        System.out.println(" 高楼的打地基100米 ");
    }

    @Override
    public void buildWalls() {
        house.setWall("墙厚20cm");
        System.out.println(" 高楼的砌墙20cm ");
    }

    @Override
    public void roofed() {
        house.setRoofed("透明屋顶");
        System.out.println(" 高楼的透明屋顶 ");
    }

    @Override
    public House buildHouse() {
        return house;
    }

}
