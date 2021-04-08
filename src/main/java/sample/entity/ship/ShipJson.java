/**
 * Copyright 2021 json.cn
 */
package sample.entity.ship;
import sample.entity.ship.EngineSlots;
import sample.entity.ship.WeaponSlots;

import java.util.List;

/**
 * Auto-generated: 2021-03-18 11:40:16
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class ShipJson {

    private List<Double> bounds;
    private List<String> builtInWings;
    private List<Double> center;
    private double collisionRadius;
    private List<EngineSlots> engineSlots;
    private int height;
    private String hullId;
    private String hullName;
    private String hullSize;
    private List<Integer> shieldCenter;
    private int shieldRadius;
    private String spriteName;
    private String style;
    private int viewOffset;
    private List<WeaponSlots> weaponSlots;
    private int width;
    public void setBounds(List<Double> bounds) {
        this.bounds = bounds;
    }
    public List<Double> getBounds() {
        return bounds;
    }

    public void setBuiltInWings(List<String> builtInWings) {
        this.builtInWings = builtInWings;
    }
    public List<String> getBuiltInWings() {
        return builtInWings;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }
    public List<Double> getCenter() {
        return center;
    }

    public void setCollisionRadius(double collisionRadius) {
        this.collisionRadius = collisionRadius;
    }
    public double getCollisionRadius() {
        return collisionRadius;
    }

    public void setEngineSlots(List<EngineSlots> engineSlots) {
        this.engineSlots = engineSlots;
    }
    public List<EngineSlots> getEngineSlots() {
        return engineSlots;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    public void setHullId(String hullId) {
        this.hullId = hullId;
    }
    public String getHullId() {
        return hullId;
    }

    public void setHullName(String hullName) {
        this.hullName = hullName;
    }
    public String getHullName() {
        return hullName;
    }

    public void setHullSize(String hullSize) {
        this.hullSize = hullSize;
    }
    public String getHullSize() {
        return hullSize;
    }

    public void setShieldCenter(List<Integer> shieldCenter) {
        this.shieldCenter = shieldCenter;
    }
    public List<Integer> getShieldCenter() {
        return shieldCenter;
    }

    public void setShieldRadius(int shieldRadius) {
        this.shieldRadius = shieldRadius;
    }
    public int getShieldRadius() {
        return shieldRadius;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }
    public String getSpriteName() {
        return spriteName;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getStyle() {
        return style;
    }

    public void setViewOffset(int viewOffset) {
        this.viewOffset = viewOffset;
    }
    public int getViewOffset() {
        return viewOffset;
    }

    public void setWeaponSlots(List<WeaponSlots> weaponSlots) {
        this.weaponSlots = weaponSlots;
    }
    public List<WeaponSlots> getWeaponSlots() {
        return weaponSlots;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }

}