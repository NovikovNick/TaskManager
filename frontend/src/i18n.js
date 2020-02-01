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
                    "DONE": "Finish",
                    "Update task":"Update task",
                    "Create task":"Create task",
                    "title":"Title",
                    "description":"Description",

                    "update_task_title":"Update task",
                    "update_task_submit_btn":"Save",

                    "create_task_title":"Create task",
                    "create_task_submit_btn":"Create",

                    "Looks good!":"Correct",
                    "Authentication failed":"Authentication failed"
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
                    "DONE": "Закончить",
                    "Update task":"Сохранить изменения",
                    "Create task":"Создать задачу",
                    "title":"Название",
                    "description":"Описание",

                    "update_task_title":"Редактирование задачи",
                    "update_task_submit_btn":"Сохранить",

                    "create_task_title":"Создать задачу",
                    "create_task_submit_btn":"Создать",

                    "Looks good!":"Корректно",

                    "Authentication failed":"Неверные логин и пароль"
                }
            }
        },
        fallbackLng: "en",

        debug: true,

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