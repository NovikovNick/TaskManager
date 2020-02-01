import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import {initReactI18next} from 'react-i18next';
import * as en from './locale/en.json'
import * as ru from './locale/ru.json'
import setting from "./config";

const options = {
    resources: {
        en: {
            translation: en.default
        },
        ru: {
            translation: ru.default
        }
    },

    //lang: navigator.language || navigator.userLanguage,

    fallbackLng: "en",

    debug: true,

    keySeparator: false,

    interpolation: {
        escapeValue: false
    },

    detection: {

        // order and from where user language should be detected
        order: ['querystring', 'cookie', 'navigator'],

        // keys or params to lookup language from
        lookupQuerystring: 'lang',
        lookupCookie: 'runninglist-lang',
        lookupFromPathIndex: 0,
        lookupFromSubdomainIndex: 0,

        // cache user language on
        caches: ['localStorage', 'cookie'],
        excludeCacheFor: ['ru', 'en'], // languages to not persist (cookie, localStorage)

        // optional expire and domain for set cookie
        cookieMinutes: 10,
        cookieDomain: setting.domain,

        // optional htmlTag with lang attribute, the default is:
        htmlTag: document.documentElement,

        // only detect languages that are in the whitelist
        checkWhitelist: true
    }
};
i18n
    .use(initReactI18next)
    .use(LanguageDetector)
    .init(options)
    .then(function(t) {
        // initialized and ready to go!
        console.log(" --> ", i18n.t('IN_PROGRESS'))
    });;

export default i18n;