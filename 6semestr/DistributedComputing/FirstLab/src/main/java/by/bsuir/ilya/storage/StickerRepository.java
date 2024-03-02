package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Sticker;
import by.bsuir.ilya.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class StickerRepository implements InMemoryRepository<Sticker>{
    private Map<Long,Sticker> stickerMemory = new HashMap<>();
    long lastId = 0;
    @Override
    public Sticker findById(long id) {
        Sticker sticker = stickerMemory.get(id);
        if(sticker!=null) {
            sticker.setId(id);
        }
        return sticker;
    }

    @Override
    public List<Sticker> findAll() {
        List<Sticker> stickerList = new ArrayList<>();
        for (Long key:stickerMemory.keySet()) {
            Sticker sticker = stickerMemory.get(key);;
            sticker.setId(key);
            stickerList.add(sticker);
        }
        return stickerList;
    }

    @Override
    public Sticker deleteById(long id) {
        Sticker result = stickerMemory.remove(id);
        if(result!=null) lastId--;
        return result;
    }

    @Override
    public boolean deleteAll() {
        stickerMemory = new HashMap<>();
        lastId = 0;
        return true;
    }

    @Override
    public Sticker insert(Sticker insertObject) {
        stickerMemory.put(lastId,insertObject);
        lastId++;
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Sticker updatingSticker) {
        boolean status = stickerMemory.replace(id,stickerMemory.get(id),updatingSticker);
        return status;
    }

    @Override
    public boolean update(Sticker updatingValue) {
        boolean status = stickerMemory.replace(updatingValue.getId(),stickerMemory.get(updatingValue.getId()),updatingValue);
        return status;
    }


}
