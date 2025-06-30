package services;

import daos.SettingDao;
import models.Order;
import models.Setting;

public class AddressService {

    /**
     * Phân tách một chuỗi địa chỉ đầy đủ thành các thành phần.
     * Giả định định dạng là: "Chi tiết, Phường/Xã, Quận/Huyện, Tỉnh/Thành phố"
     * @param fullAddress Chuỗi địa chỉ đầy đủ.
     * @param order Đối tượng Order để điền thông tin vào.
     */

    private final SettingDao settingDao;

    public AddressService() {
        this.settingDao = new SettingDao();
    }

    public void parseFullAddress(String fullAddress, Order order) {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            return;
        }

        String[] parts = fullAddress.split(", ");
        if (parts.length == 4) { // Định dạng chuẩn: Chi tiết, Phường, Huyện, Tỉnh
            order.setStreet(parts[0].trim());
            order.setWard(parts[1].trim());
            order.setDistrict(parts[2].trim());
            order.setProvince(parts[3].trim());
        } else if (parts.length == 3) { // Có thể thiếu chi tiết đường
            order.setStreet("");
            order.setWard(parts[0].trim());
            order.setDistrict(parts[1].trim());
            order.setProvince(parts[2].trim());
        } else {
            // Trường hợp không thể phân tách, gán tất cả vào street
            order.setStreet(fullAddress);
            order.setWard("");
            order.setDistrict("");
            order.setProvince("");
        }
        Setting province = settingDao.findByNameAndType(order.getProvince(), "province");
        if (province != null) {
            order.setProvinceId(province.getSettingId());
            Setting district = settingDao.findByNameAndParent(order.getDistrict(), province.getSettingId());
            if (district != null) {
                order.setDistrictId(district.getSettingId());
                Setting ward = settingDao.findByNameAndParent(order.getWard(), district.getSettingId());
                if (ward != null) {
                    order.setWardId(ward.getSettingId());
                }
            }
        }
    }

    /**
     * Ghép các thành phần địa chỉ thành một chuỗi đầy đủ.
     * @return Chuỗi địa chỉ hoàn chỉnh.
     */
    public String combineAddressParts(String street, String ward, String district, String province) {
        return String.join(", ", street, ward, district, province);
    }
}
