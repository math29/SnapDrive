package com.snapdrive.snapdrive;

/**
 * Created by crocus on 07/04/15.
 */
public class SnapMedia {

    public String get_displayName() {
        return _displayName;
    }

    public void set_displayName(String _displayName) {
        this._displayName = _displayName;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }

    public String get_size() {
        return _size;
    }

    public void set_size(String _size) {
        this._size = _size;
    }

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    private String _displayName;
    private Long _date;
    private String _size;
    private String _data;
    private String _type;

    public SnapMedia(String type, String displayName,long date, String size, String data){
        _type = type;
        _displayName = displayName;
        _data = data;
        _date = date;
        _size = size;
    }
}
