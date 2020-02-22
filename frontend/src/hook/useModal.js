import {useState} from 'react';


const useModal = () => {
    const [isActive, setIsActive] = useState(false);

    function toggle() {
        setIsActive(!isActive);
    }

    return {
        isActive,
        toggle,
    }
};

export default useModal;