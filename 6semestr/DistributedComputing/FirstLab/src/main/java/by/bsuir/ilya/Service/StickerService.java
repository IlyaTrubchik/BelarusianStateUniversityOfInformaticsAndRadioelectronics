package by.bsuir.ilya.Service;


import by.bsuir.ilya.Entity.Sticker;
import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.storage.StickerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StickerService implements IService<StickerResponseTo, StickerRequestTo> {
    @Autowired
    private StickerRepository stickerRepository;

    public StickerService(){

    }
    public List<StickerResponseTo> getAll(){
        List<Sticker> stickerList = stickerRepository.findAll();
        List<StickerResponseTo> resultList = new ArrayList<>();
        for(int i = 0;i<stickerList.size();i++)
        {
            resultList.add(StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerList.get(i)));
        }
        return resultList;
    }
    public StickerResponseTo update(StickerRequestTo updatingsticker){
        Sticker sticker = StickerMapper.INSTANCE.stickerRequestToToSticker(updatingsticker);
        if(validateSticker(sticker)) {
            boolean result = stickerRepository.update(sticker);
            StickerResponseTo responseTo = result ? StickerMapper.INSTANCE.stickerToStickerResponseTo(sticker) : null;
            return  responseTo;
        }
        else return new StickerResponseTo();
        //return responseTo;
    }
    public StickerResponseTo getById(long id){
        return StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerRepository.findById(id));
    }
    public StickerResponseTo deleteById(long id)
    {
        return StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerRepository.deleteById(id));
    }
    public StickerResponseTo add(StickerRequestTo stickerRequestTo)
    {
        Sticker sticker = StickerMapper.INSTANCE.stickerRequestToToSticker(stickerRequestTo);
        return StickerMapper.INSTANCE.stickerToStickerResponseTo(stickerRepository.insert(sticker));
    }
    private boolean validateSticker(Sticker sticker)
    {
        String name = sticker.getName();
        if (name.length()>=2 && name.length()<=32) return true;
        return false;
    }
}
