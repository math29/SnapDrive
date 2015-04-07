package com.snapdrive.snapdrive;

/**
 * Created by crocus on 07/04/15.
 */
public class SnapMedia {

    private String _displayName;
    private String _data;
    private String _type;
    private Long _date;
    private String _size;

    public SnapMedia(String type, String displayName, long date, String size, String data){
        _type = type;
        _displayName = displayName;
        _date = date;
        _size = size;
        _data = data;
    }

    public String get_displayName() {
        return _displayName;
    }

    public void set_displayName(String _displayName) {
        this._displayName = _displayName;
    }

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public Long get_date() {
        return _date;
    }

    public void set_date(Long _date) {
        this._date = _date;
    }

    public String get_size() {
        return _size;
    }

    public void set_size(String _size) {
        this._size = _size;
    }


}
