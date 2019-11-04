import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import {initReactI18next} from 'react-i18next';

i18n
//.use(Backend)
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            en: {
                translation: {
                    "mon": "mon",
                    "tue": "tue",
                    "wed": "wed",
                    "thu": "thu",
                    "fri": "fri",
                    "sat": "sat",
                    "san": "san",
                    "NONE": "Postpone",
                    "TO_DO": "To do",
                    "IN_PROGRESS": "Track time",
                    "CANCELED": "Cancel",
                    "DONE": "Finish"
                }
            },
            ru: {
                translation: {
                    "mon": "пн",
                    "tue": "вт",
                    "wed": "ср",
                    "thu": "чт",
                    "fri": "пт",
                    "sat": "сб",
                    "san": "вс",
                    "NONE": "Отложить",
                    "TO_DO": "Запланировать",
                    "IN_PROGRESS": "В процессе",
                    "CANCELED": "Отменить",
                    "DONE": "Закончить"
                }
            }
        },
        fallbackLng: "en",

        // debug: true,

        interpolation: {
            escapeValue: false, // not needed for react as it escapes by default
        },

        detection: {
            // order and from where user language should be detected
            order: ['querystring', 'cookie'],

            // keys or params to lookup language from
            lookupQuerystring: 'lang',
            lookupCookie: 'metalheart-chat-lang'
        }
    });

export default i18n;