package pl.gienius.sknera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Address;
import pl.gienius.sknera.repository.AddressRepository;

import java.util.List;

@Service
public class AddressService {

    Logger logger = LoggerFactory.getLogger(AddressService.class);

    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public void addAddress(Address address) {
        addressRepository.save(address);
    }

    public Address getAddress(Long addressId) {
        return addressRepository.getAddressById(addressId);
    }

    public Address updateAddress(Address updated){
        if (updated.getId()==null){
            logger.info("Adding address " + updated);
            addressRepository.save(updated);
            return updated;
        }
        Address toUpdate = addressRepository.getAddressById(updated.getId());
        toUpdate.setCity(updated.getCity());
        toUpdate.setCountry(updated.getCountry());
        toUpdate.setStreet(updated.getStreet());
        toUpdate.setZip(updated.getZip());
        logger.info("Address updated" + updated.toString());
        addressRepository.save(toUpdate);
        return toUpdate;
    }
}
