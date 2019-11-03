import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import {initReactI18next} from 'react-i18next';
import React from "react";

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

                    "NONE": "NONE",
                    "TO_DO": "TO_DO",
                    "IN_PROGRESS": "IN_PROGRESS",
                    "CANCELED": "CANCELED",
                    "DONE": "DONE"
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
                    "san": "вс"
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