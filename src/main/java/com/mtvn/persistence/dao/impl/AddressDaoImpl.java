package com.mtvn.persistence.dao.impl;

import com.mtvn.api.shared.PincodeApi;
import com.mtvn.common.string.StringUtils;
import com.mtvn.persistence.dao.AddressDao;
import com.mtvn.persistence.dao.template.GenericDaoImpl;
import com.mtvn.persistence.entities.Address;
import org.springframework.stereotype.Repository;

import static org.apache.commons.text.WordUtils.wrap;


@Repository("addressDao")
public class AddressDaoImpl extends GenericDaoImpl<Address, Integer> implements AddressDao {

    @Override
    public Address save(Address entity) {
        return this.saveOrUpdate(entity);
    }

    @Override
    public Address saveOrUpdate(Address entity) {
        if(entity.getPincode() != null && entity.getPincode() > 0) {
            if(!StringUtils.hasText(entity.getCity())) {
                String city = PincodeApi.getCity(entity.getPincode().toString());
                entity.setCity(city);
            }
            if(!StringUtils.hasText(entity.getState())) {
                String state = PincodeApi.getState(entity.getPincode().toString());
                entity.setState(state);
            }

            StringBuilder addressLine = new StringBuilder();
            if(StringUtils.hasText(entity.getAddressLine1()))
                addressLine.append(entity.getAddressLine1()).append(" ");
            if(StringUtils.hasText(entity.getAddressLine2()))
                addressLine.append(entity.getAddressLine2());
            String addLine = StringUtils.sanitizeAddressForBureau(addressLine.toString());
            if(StringUtils.hasText(addLine)) {
                String sLines = wrap(addLine, 40, "\n", false, " ");
                String[] addressLines = sLines.split("\n");
                if(addressLines.length > 0)
                    entity.setAddressLine1(addressLines[0]);
                if(addressLines.length > 1) {
                    StringBuilder addressLine2 = new StringBuilder();
                    for(int i = 1 ; i < addressLines.length; i++) {
                        addressLine2.append(addressLines[i]).append(" ");
                    }
                    entity.setAddressLine2(addressLine2.toString().trim());
                }
            }
        }
        return super.saveOrUpdate(entity);
    }

}
