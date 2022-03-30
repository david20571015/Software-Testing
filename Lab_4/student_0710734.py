from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))


def print_nycu_news():
    driver.get('https://www.nycu.edu.tw/')
    driver.maximize_window()

    news_css_selector = 'a[title="消息"].elementor-item'
    driver.find_element(by=By.CSS_SELECTOR, value=news_css_selector).click()

    first_news_css_selector = '#-tab > ul > li'
    driver.find_element(by=By.CSS_SELECTOR,
                        value=first_news_css_selector).click()

    title_css_selector = 'h1.single-post-title.entry-title'
    title = driver.find_element(by=By.CSS_SELECTOR,
                                value=title_css_selector).text

    print('title:')
    print(f'{title}')

    content_css_selector = 'div.entry-content.clr > p'
    contents = driver.find_elements(by=By.CSS_SELECTOR,
                                    value=content_css_selector)[1:-1]

    print('contents:')
    for content in contents:
        print(content.text)


def print_google_search():
    driver.get('https://www.google.com')

    student_id = '0710734'
    search_bar_css_selector = 'input.gLFyf.gsfi'
    driver.find_element(by=By.CSS_SELECTOR,
                        value=search_bar_css_selector).send_keys(student_id)

    search_button_css_selector = 'input.gNO89b'
    driver.find_elements(by=By.CSS_SELECTOR,
                         value=search_button_css_selector)[-1].click()

    results_css_selector = 'div.v7W49e div.yuRUbf'
    results = driver.find_elements(by=By.CSS_SELECTOR,
                                   value=results_css_selector)
    result = results[1].find_element(by=By.CSS_SELECTOR, value='h3').text

    print('result:')
    print(f'{result}')


if __name__ == '__main__':
    print('-' * 10 + 'a' + '-' * 10)
    print_nycu_news()
    driver.switch_to.new_window('tab')
    print('-' * 10 + 'b' + '-' * 10)
    print_google_search()
    driver.quit()
