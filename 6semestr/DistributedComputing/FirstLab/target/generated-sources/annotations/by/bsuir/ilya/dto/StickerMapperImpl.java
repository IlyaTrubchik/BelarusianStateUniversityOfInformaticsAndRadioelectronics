package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Sticker;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-28T20:43:09+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
public class StickerMapperImpl implements StickerMapper {

    @Override
    public Sticker stickerResponseToToSticker(StickerResponseTo stickerResponseTo) {
        if ( stickerResponseTo == null ) {
            return null;
        }

        Sticker sticker = new Sticker();

        sticker.setIssueId( stickerResponseTo.getIssueId() );
        sticker.setId( stickerResponseTo.getId() );
        sticker.setName( stickerResponseTo.getName() );

        return sticker;
    }

    @Override
    public Sticker stickerRequestToToSticker(StickerRequestTo stickerRequestTo) {
        if ( stickerRequestTo == null ) {
            return null;
        }

        Sticker sticker = new Sticker();

        sticker.setIssueId( stickerRequestTo.getIssueId() );
        sticker.setId( stickerRequestTo.getId() );
        sticker.setName( stickerRequestTo.getName() );

        return sticker;
    }

    @Override
    public StickerResponseTo stickerToStickerResponseTo(Sticker sticker) {
        if ( sticker == null ) {
            return null;
        }

        StickerResponseTo stickerResponseTo = new StickerResponseTo();

        stickerResponseTo.setIssueId( sticker.getIssueId() );
        stickerResponseTo.setId( sticker.getId() );
        stickerResponseTo.setName( sticker.getName() );

        return stickerResponseTo;
    }

    @Override
    public StickerRequestTo stickerToStickerRequestTo(Sticker sticker) {
        if ( sticker == null ) {
            return null;
        }

        StickerRequestTo stickerRequestTo = new StickerRequestTo();

        stickerRequestTo.setIssueId( sticker.getIssueId() );
        stickerRequestTo.setName( sticker.getName() );
        stickerRequestTo.setId( sticker.getId() );

        return stickerRequestTo;
    }
}
