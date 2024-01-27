package pl.gienius.sknera.service;

import org.springframework.stereotype.Service;
import pl.gienius.sknera.entity.Address;
import pl.gienius.sknera.repository.AddressRepository;

import java.util.List;

@Service
public class AddressService {

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
        Address toUpdate = addressRepository.getAddressById(updated.getId());
        toUpdate.setCity(updated.getCity());
        toUpdate.setCountry(updated.getCountry());
        toUpdate.setStreet(updated.getStreet());
        toUpdate.setZip(updated.getZip());
        addressRepository.save(toUpdate);
        return toUpdate;
    }
}
