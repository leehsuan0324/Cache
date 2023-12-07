/*
Design a data structure to perform these actions
add, delete, contain, get last, get random
*/

/*
[]
1,2,3,4
=> 1,3,4
*/
#include "bits/stdc++.h"

using namespace std;

class Cache {
public:
    struct Node
    {
        int val;
        Node* prev, *next;
        /* data */
    };
    
    vector<int> elementArray;
    unordered_map<int,pair<Node*,int>> elementMap;
    Node *head, *tail;
    
    Cache(){
        elementArray.clear();
        elementMap.clear();
        head = NULL;
        tail = NULL;
    }

    bool add(int val){
        if(elementMap.count(val)){
            return 0;
        }else{
            Node *n = new Node;
            n->prev = NULL;
            n->next = NULL;
            n->val = val;
            addNodeToTail(n);
            elementArray.push_back(val);
            elementMap[val] = {n, elementArray.size()-1};
        }
        return 1;
    }

    bool erase(int val){
        if(!elementMap.count(val)){
            return 0;
        }else{
            Node *n = elementMap[val].first;
            int index = elementMap[val].second;

            int tmp = elementArray.back();
            elementArray[index] = elementArray.back();
            elementArray.pop_back();
            elementMap[tmp].second = index;

            deleteNode(n);

            elementMap.erase(val);
        }
        return 1;
    }

    bool contain(int val){
        return elementMap.count(val);
    }

    int getLast(){
        if(tail == NULL){
            return -1;
        }
        return tail->val;
    }

    int getRandom(){
        if(elementArray.size() == 0){
            return -1;
        }
        return elementArray[rand() % elementArray.size()];
    }
    void addNodeToTail(Node *node){
        if(head == NULL){
            head = node;
            tail = node;
        }else{
            tail->next = node;
            node->prev = tail;
            tail = node;
        }
    }
    void deleteNode(Node *node){
        if(node->prev == NULL){
            head = node->next;
        }else{
            node->prev->next = node->next;
        }
        
        if(node->next == NULL){
            tail = node->prev;
        }else{
            node->next->prev = node->prev;
        }
    }
};


int main(){
    // add 1, add 2, contain 1, get last, delete 1, delete 2
    Cache c;
    c.add(1);
    c.add(2);
    cout<<c.contain(1)<<endl;
    cout<<c.getLast()<<endl;
    c.erase(1);
    c.erase(2);
}