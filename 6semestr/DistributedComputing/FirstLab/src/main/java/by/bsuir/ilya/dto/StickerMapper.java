package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Sticker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface StickerMapper {
    StickerMapper INSTANCE = Mappers.getMapper( StickerMapper.class );

    Sticker stickerResponseToToSticker(StickerResponseTo stickerResponseTo);
    Sticker stickerRequestToToSticker(StickerRequestTo stickerRequestTo);

    StickerResponseTo stickerToStickerResponseTo(Sticker sticker);

    StickerRequestTo stickerToStickerRequestTo(Sticker sticker);
}
